package com.fiftycuatro.monitoro.collectors.service

import com.fiftycuatro.monitoro.collectors.Collector
import spock.lang.Specification


class DefaultCollectorServiceTest extends Specification {

    private Collector CreateCollector(String id) {
       def collector = Mock(Collector);
       collector.id >> id;
       return collector;
    }

    DefaultCollectorService service
    def groupId1 = "SET_ID1"
    def groupId2 = "SET_ID2"
    Collector collector1
    Collector collector2

    def setup() {
        service = new DefaultCollectorService()
        collector1 = CreateCollector("COLLECTOR_ID1")
        collector2 = CreateCollector("COLLECTOR_ID2")
    }

    def "trying to get collectors when no collectors have been added returns an empty List"() {
       expect:
       0 == service.getCollectors().size();
    }

    def "trying to add a null collector causes an Illegal Argument exception"() {
        when:
        service.addCollector(null)

        then:
        thrown IllegalArgumentException
    }

    def "can add a single collector"() {
        when:
        service.addCollector(collector1)

        then:
        1 == service.getCollectors().size()
    }

    def "cannot add a collector that has already been added"() {
        when:
        service.addCollector(collector1)
        service.addCollector(collector1)

        then:
        1 == service.getCollectors().size()
    }

    def "can add multiple collectors"() {
        when:
        service.addCollector(collector1)
        service.addCollector(collector2)

        then:
        service.getCollectors().containsAll([collector1, collector2])

    }
}
