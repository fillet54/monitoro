package com.fiftyfour.metrics

import com.fiftyfour.persistence.TopicMetric
import org.joda.time.DateTime

interface MetricsService {
    List<TopicMetric> GetAveraged(String topicName, DateTime start, DateTime end, int step);
}