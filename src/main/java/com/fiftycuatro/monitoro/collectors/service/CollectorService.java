package com.fiftycuatro.monitoro.collectors.service;

import com.fiftycuatro.monitoro.collectors.Collector;
import com.fiftycuatro.monitoro.collectors.CollectorGroup;

public interface CollectorService {
    public void addToGroup(String groupId, Collector collector);
    public CollectorGroup getGroup(String groupId);
}
