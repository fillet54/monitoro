package com.fiftyfour.com.fiftyfour.metrics

import com.fiftyfour.com.fiftyfour.persistence.TopicMetric
import com.fiftyfour.com.fiftyfour.persistence.TopicMetricStore
import org.joda.time.DateTime
import spock.lang.*

class SimpleMetricServiceTest extends Specification {
    def start = DateTime.now()
    def end = start.plusSeconds(10)

    def "returns values as is when step lines up"() {
        setup:
        def storeMock = Mock(TopicMetricStore)
        storeMock.GetRange("blah", start, end) >> (0..10).collect { new TopicMetric(timeStamp: start.plusSeconds(it)) }
        def metricsService = new SimpleMetricsService(storeMock)

        when:
        def result = metricsService.GetAveraged("blah", start, end, 1000)

        then:
        result.size() == 11
    }

    def "returns values averaged when step is doubled"() {
        setup:
        def storeMock = Mock(TopicMetricStore)
        storeMock.GetRange("blah", start, end) >> (0..10).collect { new TopicMetric(timeStamp: start.plusSeconds(it), messageIn: it, messageOut: it) }
        def metricsService = new SimpleMetricsService(storeMock)

        when:
        def result = metricsService.GetAveraged("blah", start, end, 2000)

        then:
        result.size() == 6
    }

    def "returns values interpolated when step is halved"() {
        setup:
        def storeMock = Mock(TopicMetricStore)
        storeMock.GetRange("blah", start, end) >> (0..10).collect { new TopicMetric(timeStamp: start.plusSeconds(it), messageIn: it, messageOut: it) }
        def metricsService = new SimpleMetricsService(storeMock)

        when:
        def result = metricsService.GetAveraged("blah", start, end, 500)

        then:
        result.size() == 21
    }
}
