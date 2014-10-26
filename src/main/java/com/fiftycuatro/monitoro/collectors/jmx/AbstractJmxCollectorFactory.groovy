package com.fiftycuatro.monitoro.collectors.jmx

import com.fiftycuatro.monitoro.collectors.Collector;

interface AbstractJmxCollectorFactory {
    Collector createJmxAttributeCollector(String id,  String hostUrl,
                                                      String objectName, String attributeName);
}
