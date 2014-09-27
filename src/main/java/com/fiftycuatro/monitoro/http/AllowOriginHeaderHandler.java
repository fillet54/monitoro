package com.fiftycuatro.monitoro.http;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

public class AllowOriginHeaderHandler implements HttpHandler {
    private final String domain;

    public AllowOriginHeaderHandler(String domain) {
        this.domain = domain;
    }

    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
    	response.header("Access-Control-Allow-Origin", domain);
    	if (request.hasHeader("Access-Control-Request-Headers")) {
    		if (request.header("x-requested-with")
    				.equals("XMLHttpRequest")) {
    			System.out.println("Found AJAX!!!!");
    			response.header("Access-Control-Allow-Headers", "X-Requested-With");
    		}
    	}
        control.nextHandler();
    }
}
