package org.springside.modules.metrics;

import java.util.concurrent.atomic.AtomicLong;

import org.springside.modules.metrics.utils.Clock;

public class Counter {
	public static Clock clock = Clock.DEFAULT;

	private AtomicLong counter = new AtomicLong(0);

	private long totalCount = 0L;
	private long lastReportTime;

	public Counter() {
		lastReportTime = clock.getCurrentTime();
	}

	public void inc() {
		counter.incrementAndGet();
	}

	public void inc(long n) {
		counter.addAndGet(n);
	}

	public void dec() {
		counter.decrementAndGet();
	}

	public void dec(long n) {
		counter.addAndGet(-n);
	}

	public CounterMetric calculateMetric() {

		long currentCount = counter.getAndSet(0);
		long currentTime = clock.getCurrentTime();

		CounterMetric metric = new CounterMetric();

		totalCount += currentCount;
		metric.lastCount = currentCount;
		metric.totalCount = totalCount;

		long timePass = currentTime - lastReportTime;
		if (timePass > 0) {
			metric.lastRate = (currentCount * 1000) / timePass;
		}

		lastReportTime = currentTime;
		return metric;
	}
}
