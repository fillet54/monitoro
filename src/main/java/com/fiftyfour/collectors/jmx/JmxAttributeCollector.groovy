package com.fiftyfour.collectors.jmx

import com.fiftyfour.collectors.Collector
import com.fiftyfour.persistence.TimeSeriesStore

class JmxAttributeCollector implements Collector {

	private String COLLECTION_ID = "com.fiftyfour.cpu.load";
	private String JMX_OBJECT_NAME = "java.lang:type=OperatingSystem"
	private String JMX_ATTR_NAME = "SystemCpuLoad"
	
	String collectionId;
	private JmxAttribute jmxAttribute;
	private JmxAttributeService jmxService;
	
	JmxAttributeCollector(String collectionId, JmxAttribute jmxAttribute, JmxAttributeService jmxService) {
		this.collectionId = collectionId;
		this.jmxAttribute = jmxAttribute;
		this.jmxService = jmxService;
	}
	
	void collectTo(TimeSeriesStore timeSeriesStore) {
		double attribute = jmxService.asDouble(jmxAttribute);
		timeSeriesStore.save(collectionId, attribute);
	}
}
