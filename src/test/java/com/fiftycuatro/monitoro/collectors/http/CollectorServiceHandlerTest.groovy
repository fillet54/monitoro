package com.fiftycuatro.monitoro.collectors.http

import com.fiftycuatro.monitoro.collectors.Collector
import com.fiftycuatro.monitoro.collectors.service.CollectorService
import com.fiftycuatro.monitoro.http.CollectorServiceHandler
import groovy.json.JsonSlurper
import org.webbitserver.stub.StubHttpControl
import org.webbitserver.stub.StubHttpRequest
import org.webbitserver.stub.StubHttpResponse
import spock.lang.Specification

class CollectorServiceHandlerTest extends Specification {

    private def createCollector = { id ->
        def collector = Mock(Collector);
        collector.id >> id;
        return collector;
    }

    CollectorServiceHandler handler
    CollectorService service
    def collectorIds = ["COL_ID1", "COL_ID2", "COL_ID3"]
    StubHttpRequest request
    StubHttpResponse response
    StubHttpControl control

    def setup() {
        service = Mock(CollectorService)
        handler = new CollectorServiceHandler(service);
        request = new StubHttpRequest();
        response = new StubHttpResponse();
        control = new StubHttpControl();
    }

    def "returns empty list when no collectors exist"() {
        setup:
        service.getCollectors() >> []

        when:
        handler.handleHttpRequest(request, response, control);

        then:
        fromJson(response.contentsString()) == fromJson('[]');
    }

    def "returns a single entry list when a single collector exists"() {
        setup:
        service.getCollectors() >> collectorIds.first().collect(createCollector)

        when:
        handler.handleHttpRequest(request, response, control);

        then:
        fromJson(response.contentsString()) == collectorsFromJson(collectorIds.first())
    }

    def "returns multiple entries when there are multiple collectors" () {
        setup:
        service.getCollectors() >> collectorIds.collect(createCollector)

        when:
        handler.handleHttpRequest(request, response, control)

        then:
        fromJson(response.contentsString()) == collectorsFromJson(collectorIds)
    }

    def "ends the response"() {
        setup:
        service.getCollectors() >> []

        when:
        handler.handleHttpRequest(request, response, control)

        then:
        response.ended()
    }

    def "sets the Content-Type to application/json"() {
        setup:
        service.getCollectors() >> []

        when:
        handler.handleHttpRequest(request, response, control)

        then:
        response.containsHeader("Content-Type")
        response.header("Content-Type") == "application/json"
    }

    def collectorsFromJson(collectorIds) {
        collectorIds.collect({ [id: it] })
    }

    def fromJson(json) { return new JsonSlurper().parseText(json); }
}
