package com.fiftycuatro.monitoro


import com.fiftycuatro.monitoro.collectors.jmx.JmxAttribute
import com.fiftycuatro.monitoro.collectors.jmx.JmxAttributeCollector
import com.fiftycuatro.monitoro.collectors.jmx.JmxService
import com.fiftycuatro.monitoro.http.SimpleHttpProxyHandler
import com.fiftycuatro.monitoro.persistence.blueflood.BluefloodStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

import org.webbitserver.WebServers
import org.webbitserver.handler.EmbeddedResourceHandler
import org.webbitserver.handler.logging.LoggingHandler
import org.webbitserver.handler.logging.SimpleLogSink

class App {

    static void main(String[] args) {
		
		startCollections();

        def webserver = WebServers.createWebServer(8080)
                .add(new LoggingHandler(new SimpleLogSink()))
				.add("/blueflood/.*", new SimpleHttpProxyHandler("http://localhost:20000", "/blueflood")) // need to pass in filter.. better way soon
                .add(new EmbeddedResourceHandler("web"))
                .start()
                .get();
    }
	
	static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	
	static void startCollections() {
		def store = new BluefloodStore("master");
		def jmxService = new JmxService("service:jmx:rmi:///jndi/rmi://localhost:9180/jmxrmi");
		List<JmxAttributeCollector> jmxCollectors = [[id:"com.fiftycuatro.cpu.load", object: "java.lang:type=OperatingSystem", name:"SystemCpuLoad"]]
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
