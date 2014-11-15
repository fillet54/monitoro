package com.fiftycuatro.monitoro.collectors.service

import com.fiftycuatro.monitoro.collectors.Collector

class DefaultCollectorService implements CollectorService {

    private List<Collector> collectors = [];

    @Override
    List<Collector> getCollectors() {
        return collectors
    }

    @Override
    void addCollector(Collector collector) {
        if (collector == null)
            throw new IllegalArgumentException("Collector cannot be null")

        if (!collectors.contains(collector))
            collectors.add(collector)
    }
}
