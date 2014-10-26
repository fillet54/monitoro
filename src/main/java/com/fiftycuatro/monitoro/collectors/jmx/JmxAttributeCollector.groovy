package com.fiftycuatro.monitoro.collectors.jmx

import com.fiftycuatro.monitoro.collectors.Collector
import com.fiftycuatro.monitoro.persistence.TimeSeriesStore

class JmxAttributeCollector implements Collector {
	
	String id;
	private JmxAttribute jmxAttribute;
	private JmxAttributeService jmxService;
	
	JmxAttributeCollector(String collectionId, JmxAttribute jmxAttribute, JmxAttributeService jmxService) {
		this.id = collectionId;
		this.jmxAttribute = jmxAttribute;
		this.jmxService = jmxService;
	}
	
	void collectTo(TimeSeriesStore timeSeriesStore) {
		double attribute = jmxService.asDouble(jmxAttribute);
		timeSeriesStore.save(id, attribute);
	}
}
