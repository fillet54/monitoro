package com.fiftycuatro.monitoro.collectors.jmx

import com.fiftycuatro.monitoro.collectors.Collector
import com.fiftycuatro.monitoro.collectors.service.CollectorService
import groovy.json.JsonBuilder
import spock.lang.Specification

class JmxAttributeCollectorLoaderTest extends Specification {

    def "can parse a JMX type with a single host"() {
        setup:
        def config = [
                hosts: [
                        host: [ url: "hosturl" ]
                ],
                collectors: [
                        [
                                id: "collector.id",
                                name: "Collector1 Name",
                                type: "JMX_ATTRIBUTE",
                                host: "host",
                                objectName: "object.name",
                                attributeName: "attribute.name"
                        ]
                ]
        ]
        def serviceMock = Mock(CollectorService)
        def collectorMock = Mock(Collector)
        def factoryMock = Mock(AbstractJmxCollectorFactory)
        factoryMock.createJmxAttributeCollector("collector.id", "hosturl", "object.name", "attribute.name") >> collectorMock
        def loader = new JmxAttributeCollectorLoader(factoryMock)

        when:
        loader.loadConfigurationTo(asJson(config), serviceMock)

        then:
        1 * serviceMock.addCollector(collectorMock)

    }

    def asJson = { new JsonBuilder(it).toString() }
}
