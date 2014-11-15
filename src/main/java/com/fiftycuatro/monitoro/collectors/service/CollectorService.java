package com.fiftycuatro.monitoro.collectors.service;

import com.fiftycuatro.monitoro.collectors.Collector;

import java.util.List;

public interface CollectorService {
    List<Collector> getCollectors();
    void addCollector(Collector collector);
}
