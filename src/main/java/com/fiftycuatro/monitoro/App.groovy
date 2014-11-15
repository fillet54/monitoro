package com.fiftycuatro.monitoro

import com.fiftycuatro.monitoro.collectors.jmx.DefaultJmxCollectorFactory
import com.fiftycuatro.monitoro.collectors.jmx.JmxAttributeCollectorLoader
import com.fiftycuatro.monitoro.collectors.service.AggregateCollectorLoader
import com.fiftycuatro.monitoro.collectors.service.CollectorService
import com.fiftycuatro.monitoro.collectors.service.DefaultCollectorService
import com.fiftycuatro.monitoro.http.CollectorServiceHandler
import com.fiftycuatro.monitoro.http.SimpleHttpProxyHandler
import com.fiftycuatro.monitoro.persistence.blueflood.BluefloodStore
import groovy.json.JsonBuilder
import org.webbitserver.WebServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

import org.webbitserver.WebServers
import org.webbitserver.handler.EmbeddedResourceHandler
import org.webbitserver.handler.StaticFileHandler
import org.webbitserver.handler.logging.LoggingHandler
import org.webbitserver.handler.logging.SimpleLogSink

class App {

    static void main(String[] args) {

		def collectorService = new DefaultCollectorService();
		def config = processCommandLineArgs(args)
		loadCollectors(config['collectorsConfig'], collectorService)
		startCollections(collectorService);

        def webServer = WebServers.createWebServer(8080)
                .add(new LoggingHandler(new SimpleLogSink()))
				.add(new SimpleHttpProxyHandler("/blueflood/", "http://localhost:20000"))
				.add("/collectors/*", new CollectorServiceHandler(collectorService))
				addDevelopmentHandlers(args, webServer)
		        .add(new EmbeddedResourceHandler("web"))
				.start()
				.get()
    }

	static HashMap<String, Object> processCommandLineArgs(String[] args) {
		//hard code for now
		def collectorsConfig = [
				hosts: [
						blueflood: [ url: "service:jmx:rmi:///jndi/rmi://localhost:9180/jmxrmi" ]
				],
				collectors: [
						[
								id: "com.blueflood.cpu.load",
								name: "Blueflood CPU Load",
								type: "JMX_ATTRIBUTE",
								host: "blueflood",
								objectName: "java.lang:type=OperatingSystem",
								attributeName: "SystemCpuLoad"
						],
						[
								id: "com.rackspacecloud.blueflood.cache.HitRate",
								name: "Blueflood Cache Hit Rate",
								type: "JMX_ATTRIBUTE",
								host: "blueflood",
								objectName: "com.rackspacecloud.blueflood.cache:type=MetadataCache,name=Stats",
								attributeName: "HitRate"
						],
						[
								id: "com.rackspacecloud.blueflood.cache.MissRate",
								name: "Blueflood Cache Miss Rate",
								type: "JMX_ATTRIBUTE",
								host: "blueflood",
								objectName: "com.rackspacecloud.blueflood.cache:type=MetadataCache,name=Stats",
								attributeName: "MissRate"
						]
				]
		]
		return [collectorsConfig: new JsonBuilder(collectorsConfig).toString()]
	}

	static void loadCollectors(String jsonConfig, CollectorService service) {
		def loaders = [
				new JmxAttributeCollectorLoader(new DefaultJmxCollectorFactory())
		]
		new AggregateCollectorLoader(loaders)
				.loadConfigurationTo(jsonConfig, service)
	}

    private static Object addDevelopmentHandlers(String[] args, WebServer webServer) {
        if (args.contains("--development")) {
            webServer.add(new StaticFileHandler("src/main/resources/web"));
        }
		return webServer
    }

    static ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	static void startCollections(CollectorService service) {
		def store = new BluefloodStore("master");
		service.collectors.each { collector ->
			executor.scheduleAtFixedRate({collector.collectTo(store)}, 0, 1, TimeUnit.SECONDS)
		}
	}
}
