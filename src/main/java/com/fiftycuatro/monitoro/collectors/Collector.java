package com.fiftycuatro.monitoro.collectors;

import com.fiftycuatro.monitoro.persistence.TimeSeriesStore;

public interface Collector {
	public void collectTo(TimeSeriesStore store);
}
