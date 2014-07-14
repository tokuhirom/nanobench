package me.geso.nanobench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void test() throws Exception {
		new Benchmark(new StringBuilderBenchmark()).warmup(10).run(100).timethese().cmpthese();
	}

	static class StringBuilderBenchmark {

		static final int inner = 100000;

		public static void benchStringBuilder() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < inner; ++i) {
				builder.append("AAAAA");
			}
			builder.toString();
		}

		@SuppressWarnings("StringBufferMayBeStringBuilder")
		public static void benchStringBuffer() {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < inner; ++i) {
				buffer.append("AAAAA");
			}
			buffer.toString();
		}

		public static void benchStringConcat() {
			String buffer = new String();
			for (int i = 0; i < inner; ++i) {
				buffer.concat("AAAAA");
			}
			buffer.toString();
		}
	}

	@Test
	public void testRandom() throws Exception {
		new Benchmark(new MathRandomBench()).warmup(10).run(100000).timethese().cmpthese();
	}

	@Test
	public void testForEach() throws Exception {
		new Benchmark(new SumBenchmark()).warmup(10).run(100000).timethese().cmpthese();
	}

	class SumBenchmark {

		List<Integer> list = rangeList(0, 10000);

		public void benchExtendedFor() {
			@SuppressWarnings("unused")
			int s = 0;
			for (Integer i : list) {
				s += i;
			}
		}
	}

	public List<Integer> rangeList(int min, int max) {
		List<Integer> list = new ArrayList<>(max - min);
		for (int i = min; i < max; ++i) {
			list.add(i);
		}
		return list;
	}

	@Test
	public void testSleep() throws Exception {
		new Benchmark(new SleepBenchmark()).warmup(0).run(100).timethese().cmpthese();
	}

	class SleepBenchmark {

		public void benchSleep10msec() throws InterruptedException {
			Thread.sleep(10);
		}

		public void benchSleep100msec() throws InterruptedException {
			Thread.sleep(100);
		}
	}

	@Test
	public void testCountIt() throws Exception {
		new Benchmark(new MathRandomBench()).countit(1, "benchMathRandom");
	}

	class MathRandomBench {

		Random random = new Random();

		public void benchMathRandom() {
			Math.random();
		}

		public void benchRandomDoubles() {
			random.nextDouble();
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testCmpTheseNegative() throws Exception {
		new Benchmark(new ArrayListBenchmark()).enableDebugging().warmup(1).runByTime(0.1).cmpthese()
						.timethese();
	}

	class ArrayListBenchmark {

		final int[] ary;
		final List<Integer> list;

		public ArrayListBenchmark() {

			int size = 1000000;
			this.list = new ArrayList<Integer>(size);
			this.ary = new int[size];
			for (int i = 0; i < size; ++i) {
				this.list.add(i);
				this.ary[i] = i;
			}
		}

		public void benchArray() {
			for (int i = 0; i < 1000; ++i) {
				int s = 0;
				for (int a : ary) {
					s += a;
				}
			}
		}

		public void benchList() {
			for (int i = 0; i < 1000; ++i) {
				int s = 0;
				for (int a : list) {
					s += a;
				}
			}
		}
	}

}
