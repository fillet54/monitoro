package com.fiftycuatro.monitoro.collectors;

import com.fiftycuatro.monitoro.persistence.TimeSeriesStore;

public interface Collector {
    public String getId();
	public void collectTo(TimeSeriesStore store);
}
