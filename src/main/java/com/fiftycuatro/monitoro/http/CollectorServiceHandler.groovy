package com.fiftycuatro.monitoro.http

import com.fiftycuatro.monitoro.collectors.service.CollectorService
import groovy.json.JsonBuilder
import org.webbitserver.HttpControl
import org.webbitserver.HttpHandler
import org.webbitserver.HttpRequest
import org.webbitserver.HttpResponse

class CollectorServiceHandler implements HttpHandler {

    private final CollectorService service;
    CollectorServiceHandler(CollectorService service) {
       this.service = service;
    }

    @Override
    void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        response
        .header('Content-Type', 'application/json')
        .content(collectorsAsJson())
        .end();
    }

    private String collectorsAsJson() {
        return new JsonBuilder(service.collectors).toString()
    }

}
