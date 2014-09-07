package com.fiftyfour.com.fiftyfour.metrics

import com.fiftyfour.com.fiftyfour.persistence.TopicMetric
import com.fiftyfour.com.fiftyfour.persistence.TopicMetricStore
import org.joda.time.DateTime

class SimpleMetricsService implements  MetricsService {

    private TopicMetricStore topicMetricStore;

    SimpleMetricsService(TopicMetricStore topicMetricStore) {
        this.topicMetricStore = topicMetricStore;
    }

    List<TopicMetric> GetAveraged(String topicName, DateTime start, DateTime end, int step) {
        def metrics = topicMetricStore.GetRange(topicName, start, end);

        List<TopicMetric> results = []
        List<TopicMetric> intervalSet = []
        DateTime intervalStart = null
        TopicMetric previous = null
        metrics.each { current ->
            if (intervalStart != null) {
               def delta = current.timeStamp.getMillis() - intervalStart.getMillis()
               if (delta >= step) {
                   results.add(average(intervalSet))

                   // Interpolate missing values by using previous value
                   def numToInterpolate = (int)(delta / step)
                   if (numToInterpolate > 0) {
                       (1..<numToInterpolate).each { results.add(previous) }
                   }

                   intervalSet = [current]
                   intervalStart = current.timeStamp
               }
               else {
                   intervalSet.add(current)
               }
            }
            else {
                intervalSet.add(current)
                intervalStart = current.timeStamp
            }

            previous = current
        }
        if (intervalSet.size() > 0) {
            results.add(average(intervalSet))
        }
        return results;
    }

    private TopicMetric average(List<TopicMetric> metrics) {
        TopicMetric result = new TopicMetric(messageIn: 0, messageOut: 0)
        metrics.each {
            result.messageIn += it.messageIn
            result.messageOut += it.messageOut
        }
        result.messageIn = result.messageIn / metrics.size()
        result.messageOut = result.messageOut / metrics.size()
        return result
    }
}
