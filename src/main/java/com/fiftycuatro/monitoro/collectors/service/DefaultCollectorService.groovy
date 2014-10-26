package com.fiftycuatro.monitoro.collectors.service

import com.fiftycuatro.monitoro.collectors.Collector
import com.fiftycuatro.monitoro.collectors.CollectorGroup

class DefaultCollectorService implements CollectorService {

    private Map<String, CollectorGroup> groups = new HashMap<>();

    void addToGroup(String groupId, Collector collector) {
       if (groupId == null)
           throw new IllegalArgumentException("GroupId cannot be null")
       if (collector == null)
           throw new IllegalArgumentException("Collector cannot be null")

       if (groups[groupId] == null)
           groups[groupId] = new CollectorGroup()

        groups[groupId].add(collector)
    }

    CollectorGroup getGroup(String groupId) {
        return groups.containsKey(groupId) ? groups[groupId] : new HashSet<>()
    }
}
