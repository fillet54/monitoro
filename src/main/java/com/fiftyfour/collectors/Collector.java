package com.fiftyfour.collectors;

import com.fiftyfour.persistence.TimeSeriesStore;

public interface Collector {
	public void collectTo(TimeSeriesStore store);
}
