package com.fiftyfour.persistence.blueflood

import com.fiftyfour.persistence.TimeSeriesStore;
import com.google.gson.GsonBuilder
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime
import org.joda.time.Duration

class BluefloodStore implements TimeSeriesStore {
	
	class BluefloodDoubleCollection {
		long collectionTime
		long ttlInSeconds
		double metricValue
		String metricName
	}
	
	String tenentId;

	BluefloodStore(String tenentId) {
		this.tenentId = tenentId;
	}

	
	public void save(String id, double value) {
		try {
			def json = GetCollectionIngestJson(id, value);
			postToBlueflood(this.url, json);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String GetCollectionIngestJson(String id, double value) {
		def gson = new GsonBuilder().create();
		def collection = new BluefloodDoubleCollection(collectionTime: DateTime.now().getMillis(),
						 	 	ttlInSeconds: Duration.standardDays(2).getStandardSeconds(),
								metricValue: value,
								metricName: id);
        return gson.toJson([collection]);
	}
	
	private String getUrl() {
		return "http://localhost:19000/v2.0/$tenentId/ingest"
	}
	
	private void postToBlueflood(String url, String json) {
		println ("Posting $url with $json")
		def client = HttpClientBuilder.create().build();
		def post = createPost(url, json);
		def response = client.execute(post);
	}
	
	private HttpPost createPost(String url, String json) {
		def post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        post.setHeader("X-Stream" , "true");
		post.setEntity(new StringEntity(json));
		return post;
	}
}
