package me.geso.nanobench;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2014 Tokuhiro Matsuno, http://64p.org/
 *
 * This is free software; you can redistribute it and/or modify it under the
 * same terms as the Perl 5 programming language system itself.
 */
public class Benchmark {

	private final Map<Long, Score> emptyLoopCache = new HashMap<>();
	private boolean debug = false;
	private final Object suite;

	private final Method[] methods;

	public Benchmark(Object suite) {
		this.suite = suite;
		Method[] allMethods = suite.getClass().getMethods();
		ArrayList<Method> methodsList = new ArrayList<>();
		for (Method method : allMethods) {
			if (method.getName().startsWith("bench") || method.isAnnotationPresent(Bench.class)) {
				methodsList.add(method);
			}
		}
		this.methods = methodsList.toArray(new Method[methodsList.size()]);
	}

	public Benchmark enableDebugging() {
		debug = true;
		return this;
	}

	/**
	 * Run the code for warming up.
	 *
	 * @param ntimes
	 * @return
	 * @throws Exception
	 */
	public Benchmark warmup(int ntimes) throws Exception {
		System.out.println("Warm up: " + ntimes + "\n");

		for (Method method : methods) {
			for (int i = 0; i < ntimes; ++i) {
				method.invoke(this.suite);
			}
		}

		DEBUG("Warm up done.");
		return this;
	}

	private Score measureEmptyLoop(long ntimes) throws Exception {
		if (!emptyLoopCache.containsKey(ntimes)) {
			Score empty = this.runloop(ntimes,
					Benchmark.class.getMethod("emptyMethod"));
			emptyLoopCache.put(ntimes, empty);
			return empty;
		} else {
			DEBUG("Hit empty loop cache.");
			return emptyLoopCache.get(ntimes);
		}
	}

	public static void emptyMethod() {
	}

	/**
	 * Run all scenarios.
	 *
	 * @param ntimes
	 * @return
	 * @throws Exception
	 */
	public Result run(int ntimes) throws Exception {
		List<ScenarioResult> results = new ArrayList<>();
		for (Method method : this.methods) {
			Score t = timeit(ntimes, method);
			results.add(new ScenarioResult(method.getName(), t));
		}

		return new Result(results);
	}

	public Result runByTime(double d) throws Exception {
		List<ScenarioResult> results = new ArrayList<>();
		for (Method method : methods) {
			DEBUG("Running " + method.getName());
			Score t = countit(d, method);
			results.add(new ScenarioResult(method.getName(), t));
		}

		return new Result(results);
	}

	/**
	 * Clear all cached times.
	 */
	public void clearAllCache() {
		emptyLoopCache.clear();
	}

	/**
	 * Clear the cached time for COUNT rounds of the null loop.
	 *
	 * @param ntimes
	 */
	public void clearCache(long ntimes) {
		emptyLoopCache.remove(ntimes);
	}

	/**
	 * <i>ntimes</i> is the number of times to run the loop, and <i>code</i> is
	 * the code to run. <i>code</i> may be either a code reference or a string
	 * to be eval'd; either way it will be run in the caller's package.
	 *
	 * @param ntimes
	 * @param method
	 * @return elapsed time in nanoseconds
	 * @throws Exception
	 */
	public Score timeit(long ntimes, Method method) throws Exception {
		Score empty = this.measureEmptyLoop(ntimes);
		Score score = this.runloop(ntimes, method);

		return score.diff(empty);
	}

	public Score countit(double tmax, String methodName) throws Exception {
		return this.countit(tmax, this.suite.getClass().getMethod(methodName));
	}

	public Score countit(double tmax, Method method) throws Exception {
		if (tmax < 0.1) {
			throw new IllegalArgumentException(
					"timelimit cannot be less than '0.1'.");
		}

		// First find the minimum $n that gives a significant timing.
		int zeros = 0;
		long n = 1;
		double tc = 0.0;
		for (;; n *= 2) {
			DEBUG("Finding minimum n: " + n);

			Score td = timeit(n, method);
			tc = (double) td.cputime / 1_000_000_000.0;
			DEBUG(String.format("TC: %.8f", tc));
			if (tc <= 0.01 && n > 1024) {
				if (++zeros > 16) {
					throw new RuntimeException(
							"Timing is consistently zero in estimation loop, cannot benchmark. N="
									+ n);
				}
			} else {
				zeros = 0;
			}
			if (tc > 0.1) {
				break;
			}
			if (n < 0) {
				throw new RuntimeException("Overflow?");
			}
		}

		long nmin = n;
		// Get $n high enough that we can guess the final $n with some accuracy.
		double tpra = 0.1 * tmax; // Target/time practice.
		while (tc < tpra) {
			/*
			 * # The 5% fudge is to keep us from iterating again all # that
			 * often (this speeds overall responsiveness when $tmax is big # and
			 * we guess a little low). This does not noticeably affect #
			 * accuracy since we're not counting these times.
			 */
			n = (int) (tpra * 1.05 * n / tc); // Linear approximation.
			Score td = timeit(n, method);
			double new_tc = td.cputime / 1_000_000_000.0;
			// Make sure we are making progress.
			tc = new_tc > 1.2 * tc ? new_tc : 1.2 * tc;
		}

		// Now, do the 'for real' timing(s), repeating until we exceed
		// the max.
		Score result = new Score(0, 0, 0, 0, 0);

		// The 5% fudge is because $n is often a few % low even for routines
		// with stable times and avoiding extra timeit()s is nice for
		// accuracy's sake.
		n = (int) (n * (1.05 * tmax / tc));
		zeros = 0;
		while (true) {
			Score td = timeit(n, method);
			result = result.add(td);
			if ((result.cputime / 1_000_000_000.0) >= tmax) {
				break;
			}
			if (result.cputime == 0) {
				if (++zeros > 16) {
					throw new RuntimeException(
							"Timing is consistently zero in estimation loop, cannot benchmark. N="
									+ n);
				}
			} else {
				zeros = 0;
			}
			if (result.cputime < 10_000_000) {
				result.cputime = 10_000_000;
			}
			double r = tmax / (result.cputime / 1_000_000_000.0) - 1; // Linear
			// approximation.
			n = (int) (r * result.iters);
			if (n < nmin) {
				n = nmin;
			}
		}
		return result;
	}

	private void DEBUG(String message) {
		if (debug) {
			System.out.println("[DEBUG] " + message);
		}
	}

	private Score runloop(long ntimes, Method method) throws InvocationTargetException, IllegalAccessException {
		System.gc();
		System.runFinalization();

		long real1 = System.nanoTime();
		long cputime1 = this.getCpuTime();
		long usertime1 = this.getUserTime();
		long systemtime1 = this.getSystemTime();
		for (int i = 0; i < ntimes; ++i) {
			method.invoke(this.suite);
		}
		long real2 = System.nanoTime();
		long cputime2 = this.getCpuTime();
		long usertime2 = this.getUserTime();
		long systemtime2 = this.getSystemTime();

		Score score = new Score(//
				real2 - real1, //
				cputime2 - cputime1, //
				usertime2 - usertime1, //
				systemtime2 - systemtime1, //
				ntimes);

		System.gc();
		System.runFinalization();

		return score;
	}

	/**
	 * Get CPU time in nanoseconds.
	 */
	public long getCpuTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean
				.getCurrentThreadCpuTime() : 0L;
	}

	/**
	 * Get user time in nanosecond
	 *
	 * @return
	 */
	long getUserTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean
				.getCurrentThreadUserTime() : 0L;
	}

	/**
	 * Get system time in nanoseconds.
	 */
	long getSystemTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? (bean
				.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
				: 0L;
	}

}
