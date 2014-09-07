package com.fiftyfour.com.fiftyfour.persistence

import org.joda.time.DateTime;

interface TopicMetricStore {
    void Save(String topicName, TopicMetric metric);
    List<TopicMetric> GetRange(String topicName, DateTime start, DateTime end);
}
