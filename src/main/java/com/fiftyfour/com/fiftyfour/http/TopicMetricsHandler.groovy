package com.fiftyfour.com.fiftyfour.http

import com.fiftyfour.com.fiftyfour.metrics.MetricsService
import com.google.gson.GsonBuilder
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.webbitserver.HttpControl
import org.webbitserver.HttpHandler
import org.webbitserver.HttpRequest
import org.webbitserver.HttpResponse

import java.nio.charset.Charset

class TopicMetricsHandler implements HttpHandler {

    private final MetricsService metricsService
    private final String contentType
    private final charset

    TopicMetricsHandler (MetricsService metricsService) {
        this.metricsService = metricsService
        this.charset = Charset.forName("UTF-8")
        this.contentType = "text/json"
    }

    @Override
    void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl httpControl) throws Exception {

        def topic = getTopic(request.uri())
        def start = request.queryParam('start')
        def end = request.queryParam('stop')
        def step = Integer.parseInt(request.queryParam('step'))

        def p = ISODateTimeFormat.dateTimeParser()
        def topicMetrics = metricsService.GetAveraged(topic, p.parseDateTime(start), p.parseDateTime(end), step)
        def targetMetrics = topicMetrics.collect { new Metric(value: it.messageIn) }
        def json = toJson(targetMetrics)

        response.charset(charset)
                .header("Content-Type", "$contentType; charset=${charset.name()}")
                .header("Content-Length", json.size())
                .content(json)
                .end();
    }

    String getTopic(String uri) {
        return uri.tokenize('?')[0].tokenize('/')[-1]
    }

    String toJson(List<Metric> metrics) {
        def gson = new GsonBuilder().create()
        return gson.toJson(metrics)
    }

    class Metric {
        int value
    }
}
