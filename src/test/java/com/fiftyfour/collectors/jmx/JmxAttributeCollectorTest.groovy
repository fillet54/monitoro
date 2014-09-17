package com.fiftyfour.collectors.jmx

import spock.lang.Specification
import com.fiftyfour.persistence.TimeSeriesStore

class JmxAttributeCollectorTest extends Specification {

	def "collectTo will collect attribute and save to store"() {
		setup:
		def COLLECTED_VALUE = 12.5
		def store = Mock(TimeSeriesStore)
		def jmxService = Mock(JmxAttributeService)
		def attribute = new JmxAttribute(objectName:"Object", attributeName:"Attribute")
		jmxService.asDouble(attribute) >> COLLECTED_VALUE;
		def collector = new JmxAttributeCollector("ID", attribute, jmxService)
		
		when:
		collector.collectTo(store)
		
		then:
		1 * store.save("ID", COLLECTED_VALUE)
	}
}
