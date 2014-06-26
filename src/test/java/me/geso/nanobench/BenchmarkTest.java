package me.geso.nanobench;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.geso.nanobench.Benchmark;

import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void test() throws Exception {
		final int inner = 100000;
		new Benchmark().add("StringBuilder", () -> {
			StringBuilder builder = new StringBuilder();
			IntStream.rangeClosed(0, inner).forEach(i -> {
				builder.append("AAAAA");
			});
			builder.toString();
		}).add("StringBuffer", () -> {
			StringBuffer buffer = new StringBuffer();
			IntStream.rangeClosed(0, inner).forEach(i -> {
				buffer.append("AAAAA");
			});
			buffer.toString();
		}).add("String.concat", () -> {
			String buffer = new String();
			IntStream.rangeClosed(0, inner).forEach(i -> {
				buffer.concat("AAAAA");
			});
			buffer.toString();
		}).warmup(10).run(100).timethese().cmpthese();
	}

	@Test
	public void testRandom() throws Exception {
		Random random = new Random();
		new Benchmark().add("Random.doubles", () -> {
			random.nextDouble();
		}).add("Math.random", () -> {
			Math.random();
		}).warmup(10).run(100000).timethese().cmpthese();
	}

	@Test
	public void testForEach() throws Exception {
		List<Integer> list = IntStream.range(0, 10000).mapToObj(i -> i)
				.collect(Collectors.toList());
		new Benchmark().add("extended for", () -> {
			@SuppressWarnings("unused")
			int s = 0;
			for (Integer i : list) {
				s += i;
			}
		}).add("stream", () -> {
			list.stream().mapToInt(i -> i).sum();
		}).warmup(10).run(100000).timethese().cmpthese();
	}

	@Test
	public void testSleep() throws Exception {
		new Benchmark().add("Sleep 10msec", () -> {
			Thread.sleep(10);
		}).add("Sleep 100msec", () -> {
			Thread.sleep(100);
		}).warmup(0).run(100).timethese().cmpthese();
	}

	@Test
	public void testCountIt() throws Exception {
		new Benchmark().countit(1, () -> {
			Math.random();
		});
	}

	@SuppressWarnings("unused")
	@Test
	public void testCmpTheseNegative() throws Exception {
		int[] ary = IntStream.range(0, 1_000_000).toArray();
		List<Integer> list = IntStream.range(0, 1_000_000).mapToObj(i -> i)
				.collect(Collectors.toList());
		new Benchmark().add("array", () -> {
			for (int i = 0; i < 1000; ++i) {
				int s = 0;
				for (int a : ary) {
					s += a;
				}
			}
		}).add("list", () -> {
			for (int i = 0; i < 1000; ++i) {
				int s = 0;
				for (int a : list) {
					s += a;
				}
			}
		}).enableDebugging().warmup(1).runByTime(0.1).cmpthese()
				.timethese();
	}

}
