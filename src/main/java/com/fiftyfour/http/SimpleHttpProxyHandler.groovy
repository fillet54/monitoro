package com.fiftyfour.http

import java.io.IOException;
import java.util.concurrent.ExecutorService

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import static java.util.concurrent.Executors.newFixedThreadPool;
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder;

class SimpleHttpProxyHandler implements HttpHandler {

	private String baseUrl;
	private String strip;
	private ExecutorService proxyThread;
	
	SimpleHttpProxyHandler (String baseUrl, String strip) {
		this.baseUrl = baseUrl;
		this.strip = strip;
		proxyThread = newFixedThreadPool(10);
	}
	
	void handleHttpRequest(HttpRequest request, HttpResponse response,
			HttpControl control) throws Exception {
		proxyThread.execute(createProxyWorker(this.baseUrl, request, response, control))	
	}
	
	private ProxyWorker createProxyWorker(String baseUrl, HttpRequest request, 
			HttpResponse response, HttpControl control) {
	    
		def proxiedPath = request.uri() - strip;
		def proxiedUrl = "$baseUrl$proxiedPath"
		println "Proxy request for $proxiedUrl";
		return new ProxyWorker(proxiedUrl, request, response);
	}
			
	class ProxyWorker implements Runnable {
		String url;
		HttpRequest request;
		HttpResponse response;
		HttpControl control;
		
		ProxyWorker(String url, HttpRequest request, 
			HttpResponse response) {
			this.url = url;
			this.request = request;
			this.response = response;
		}
			
		void run() {
			def client = HttpClientBuilder.create().build()
			def get = new HttpGet(url);
			request.allHeaders().each { header ->
				get.setHeader(header.key, header.value);
			}
			
			def proxiedResponse = client.execute(get);
			
			proxiedResponse.getAllHeaders().each { header ->
				response.header(header.name, header.value);
			}
			
			def content = proxiedResponse.getEntity().getContent().getText();
			
			response
			.content(content)
			.end();
		}
		
	}
			
	
}
