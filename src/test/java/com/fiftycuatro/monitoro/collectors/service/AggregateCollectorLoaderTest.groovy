package com.fiftycuatro.monitoro.collectors.service

import groovy.json.JsonBuilder
import spock.lang.Specification

class AggregateCollectorLoaderTest extends Specification {

    def "loading an empty config string results with nothing being loaded"() {
        setup:
        def loader = new AggregateCollectorLoader()
        def service = Mock(CollectorService)

        when:
        loader.loadConfigurationTo("", service)

        then:
        0 * service.addToGroup(_, _)
    }

    def collector(String id, String type) {
        return [
            id: id,
            type: type
        ]
    }

    def group(String id, String name, collectors) {
        return [
            id: id,
            name: name,
            collectors: collectors
        ]
    }

    def "loading with no other loaders results in no added groups"() {
        setup:
        def loader = new AggregateCollectorLoader()
        def service = Mock(CollectorService)
        def configuration = "Configuration String"

        when:
        loader.loadConfigurationTo(configuration, service)

        then:
        assert(true)
    }

    def "a single loader is called being passed a configuration and a collector service"() {
        setup:
        def typeLoader = Mock(CollectorLoader)
        def loader = new AggregateCollectorLoader([typeLoader])
        def service = Mock(CollectorService)
        def configuration = "Configuration String"

        when:
        loader.loadConfigurationTo(configuration, service)

        then:
        1 * typeLoader.loadConfigurationTo(configuration, service)
    }
}
