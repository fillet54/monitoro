package com.fiftycuatro.monitoro.collectors.jmx

import com.fiftycuatro.monitoro.collectors.Collector

class DefaultJmxCollectorFactory implements AbstractJmxCollectorFactory {

    private HashMap<String, JmxAttributeService> attributeServices = [];

    @Override
    Collector createJmxAttributeCollector(String id, String hostUrl, String objectName, String attributeName) {
        def attributeService = attributeServiceForHost(hostUrl)
        def attribute = new JmxAttribute(objectName: objectName, attributeName: attributeName)
        return new JmxAttributeCollector(id, attribute, attributeService)
    }

    private JmxAttributeService attributeServiceForHost(String hostUrl) {
        if (!attributeServices.containsKey(hostUrl))
            attributeServices[hostUrl] = new JmxService(hostUrl);

        return attributeServices[hostUrl];
    }
}
