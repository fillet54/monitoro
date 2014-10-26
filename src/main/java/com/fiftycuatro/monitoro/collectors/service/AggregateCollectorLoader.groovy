package com.fiftycuatro.monitoro.collectors.service

class AggregateCollectorLoader implements CollectorLoader{

    private List<CollectorLoader> loaders;

    AggregateCollectorLoader(List<CollectorLoader> loaders) {
        this.loaders = loaders;
    }

    void loadConfigurationTo(String configuration, CollectorService service) {
        loaders.each { loader ->
            loader.loadConfigurationTo(configuration, service)
        }
    }
}
