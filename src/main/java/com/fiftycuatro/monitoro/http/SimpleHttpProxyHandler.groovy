package com.fiftycuatro.monitoro.http

import java.util.concurrent.ExecutorService
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse

import static java.util.concurrent.Executors.newFixedThreadPool;
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder;

class SimpleHttpProxyHandler implements HttpHandler {

	private final String proxyPass;
	private final String location;
	private final ExecutorService proxyThread;
	
	SimpleHttpProxyHandler (String location, String proxyPass) {
		this.proxyPass = proxyPass;
		this.location = location;
		proxyThread = newFixedThreadPool(10);
	}

    @Override
	void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control)
            throws Exception {
        if (pathIsAMatch(request)){
            proxyThread.execute(createProxyWorker(request, response))
        }
        else {
            control.nextHandler();
        }
	}

    public Boolean pathIsAMatch(HttpRequest request){
        try {
            String path = URI.create(request.uri()).getPath()
            if (path.startsWith(location))
                return true
        }
        catch (IllegalArgumentException e) {
        }
        return false;
    }

	private ProxyWorker createProxyWorker(HttpRequest request, HttpResponse response) {
		def proxyPath = request.uri() - location;
		def proxyUrl = "$proxyPass/$proxyPath"
        println "ReverseProxy ${request.uri()} => $proxyUrl"
		return new ProxyWorker(proxyUrl, request, response);
	}
			
	class ProxyWorker implements Runnable {
		String url;
		HttpRequest request;
		HttpResponse response;
		
		ProxyWorker(String url, HttpRequest request, HttpResponse response) {
			this.url = url;
			this.request = request;
			this.response = response;
		}
			
		void run() {
			def client = HttpClientBuilder.create().build()

            def method;
            if (request.method() == 'GET')
			    method = new HttpGet(url);
            else
                method = new HttpPost(url);

			request.allHeaders().each { header ->
				method.setHeader(header.key, header.value);
			}
			
			def proxyResponse = client.execute(method);
			
			proxyResponse.getAllHeaders().each { header ->
				response.header(header.name, header.value);
			}
			
			def content = proxyResponse.getEntity().getContent().getText();
			
			response
			.content(content)
			.end();
		}	
	}
}
