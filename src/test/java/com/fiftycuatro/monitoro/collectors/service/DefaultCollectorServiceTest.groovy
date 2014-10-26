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

    def "trying to get a non-existent set returns an empty set"() {
        expect:
        0 == service.getGroup("UNKNOWN_ID").size()
    }

    def "trying to add a null collector causes an Illegal Argument exception"() {
        when:
        service.addToGroup(groupId1, null)

        then:
        thrown IllegalArgumentException
    }

    def "trying to add with a null group ID causes an Illegal Argument exception"() {
        when:
        service.addToGroup(null, collector1)

        then:
        thrown IllegalArgumentException
    }


    def "can add a single collector"() {
        when:
        service.addToGroup(groupId1, collector1)

        then:
        1 == service.getGroup(groupId1).size()
    }

    def "cannot add a collector that has already been added"() {
        when:
        service.addToGroup(groupId1, collector1)
        service.addToGroup(groupId1, collector1)

        then:
        1 == service.getGroup(groupId1).size()
    }

    def "can add multiple collectors"() {
        when:
        service.addToGroup(groupId1, collector1)
        service.addToGroup(groupId1, collector2)

        then:
        service.getGroup(groupId1).containsAll([collector1, collector2])

    }

    def "can add single collector to multiple collectors sets"() {
        when:
        service.addToGroup(groupId1, collector1)
        service.addToGroup(groupId2, collector1)

        then:
        service.getGroup(groupId1) == service.getGroup(groupId2)
    }
}
