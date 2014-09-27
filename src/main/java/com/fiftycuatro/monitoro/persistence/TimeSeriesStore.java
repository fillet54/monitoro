package com.fiftycuatro.monitoro.persistence;

public interface TimeSeriesStore {
	public void save(String id, double value);
}
