package com.fiftyfour

import com.fiftyfour.com.fiftyfour.http.TopicMetricsHandler
import com.fiftyfour.com.fiftyfour.metrics.MetricsService
import com.fiftyfour.com.fiftyfour.metrics.SimpleMetricsService
import com.fiftyfour.com.fiftyfour.persistence.FileTopicMetricStore
import com.fiftyfour.com.fiftyfour.persistence.TopicMetric
import com.fiftyfour.com.fiftyfour.persistence.TopicMetricStore
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

        // Just put random data for now
        def val = 5
        def start = DateTime.now().minusHours(12)
        (0..86400).each { // days worth of data
            def topicMetric = new TopicMetric(timeStamp: start.plusSeconds(it), messageIn: val, messageOut: val)
            store.Save("Sample", topicMetric)

            if ((it % 30) == 0) { // change value every 30 seconds
                val = Math.floor(Math.random()*10)
            }
        }
        // end of random data

        def webserver = WebServers.createWebServer(8080)
                .add(new LoggingHandler(new SimpleLogSink()))
                .add(new EmbeddedResourceHandler("web"))
                .add("/metrics/topic/.*", new TopicMetricsHandler(metricService))
                .start()
                .get();
    }
}
