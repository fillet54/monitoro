package com.fiftyfour.collectors

import com.fiftyfour.persistence.TimeSeriesStore
import org.joda.time.Duration

class CollectionSchedule implements Runnable {

	Duration frequency;
	Collector collector;
	TimeSeriesStore store;
	
	private isRunning = false;
	
	void terminate() {
		isRunning = false;
	}
	
	void run() {
		isRunning = true;
		while(!isRunning) {
			collector.collectTo(store);
			scheduleNextCollection();
		}
	}

	private void scheduleNextCollection() {
		def timeSinceLast = new Duration(collectionStart, DateTime.now());
		def timeUntilNext = frequency.minus(timeSinceLast);
		
		if (timeUntilNext.millis > 0) {
			Thread.sleep(timeUntilNext.millis);
		}
	}
}
