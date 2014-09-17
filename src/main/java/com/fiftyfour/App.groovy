package com.fiftyfour

import com.fiftyfour.collectors.CollectionSchedule
import com.fiftyfour.collectors.jmx.JmxAttribute
import com.fiftyfour.collectors.jmx.JmxAttributeCollector
import com.fiftyfour.collectors.jmx.JmxService
import com.fiftyfour.http.AllowOriginHeaderHandler
import com.fiftyfour.http.SimpleHttpProxyHandler
import com.fiftyfour.http.TopicMetricsHandler
import com.fiftyfour.metrics.MetricsService
import com.fiftyfour.metrics.SimpleMetricsService
import com.fiftyfour.persistence.FileTopicMetricStore
import com.fiftyfour.persistence.TopicMetric
import com.fiftyfour.persistence.TopicMetricStore
import com.fiftyfour.persistence.blueflood.BluefloodStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime
import org.webbitserver.WebServers
import org.webbitserver.handler.EmbeddedResourceHandler
import org.webbitserver.handler.logging.LoggingHandler
import org.webbitserver.handler.logging.SimpleLogSink

class App {

    static TopicMetricStore store;
    static MetricsService metricService;

    static void main(String[] args) {
        store = new FileTopicMetricStore()
        metricService = new SimpleMetricsService(store)
		
		startCollections();

        def webserver = WebServers.createWebServer(8080)
                .add(new LoggingHandler(new SimpleLogSink()))
				.add("/blueflood/.*", new SimpleHttpProxyHandler("http://localhost:20000", "/blueflood")) // need to pass in filter.. better way soon
                .add(new EmbeddedResourceHandler("web"))
                .add("/metrics/topic/.*", new TopicMetricsHandler(metricService))
                .start()
                .get();
    }
	
	static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	
	static void startCollections() {
		def store = new BluefloodStore("master");
		def jmxService = new JmxService("service:jmx:rmi:///jndi/rmi://localhost:9180/jmxrmi");
		List<JmxAttributeCollector> jmxCollectors = [[id:"com.fiftyfour.cpu.load", object: "java.lang:type=OperatingSystem", name:"SystemCpuLoad"]]
							.collect {
								new JmxAttributeCollector(it["id"], new JmxAttribute(objectName:it["object"], attributeName:it["name"]), jmxService)
							}
						
		jmxCollectors.each {
			executor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() { it.collectTo(store) }}, 0, 1, TimeUnit.SECONDS)
		}
	}
}
