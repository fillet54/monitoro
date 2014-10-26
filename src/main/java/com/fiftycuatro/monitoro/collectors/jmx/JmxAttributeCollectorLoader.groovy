package com.fiftycuatro.monitoro.collectors.jmx

import com.fiftycuatro.monitoro.collectors.service.CollectorLoader
import com.fiftycuatro.monitoro.collectors.service.CollectorService
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class JmxAttributeCollectorLoader implements CollectorLoader{

    private AbstractJmxCollectorFactory collectorFactory

    JmxAttributeCollectorLoader(AbstractJmxCollectorFactory collectorFactory) {
        this.collectorFactory = collectorFactory
    }

    @Override
    void loadConfigurationTo(String configuration, CollectorService service) {
        def parsed = new JsonSlurper().parseText(configuration);

        parsed.collectorGroups.each { group ->
            group.collectors.each { collector ->
                def jmxCollector = collectorFactory.createJmxAttributeCollector(
                        collector.id,
                        parsed.hosts[collector.host].url,
                        collector.objectName,
                        collector.attributeName
                )
                service.addToGroup(group.id, jmxCollector)
            }
        }
    }
}
