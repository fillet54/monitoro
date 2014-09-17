package com.fiftyfour.persistence;

public interface TimeSeriesStore {
	public void save(String id, double value);
}
