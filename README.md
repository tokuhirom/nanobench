nanobench
=========

This is a tiny benchmarking library for Java 8.

## SYNOPSIS

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
      }).warmup(1).runByTime(0.1).cmpthese().timethese();

Output:

        Comparison chart:
        
                          Rate  array             list
          array  63725011709/s     --  14202450711914%
           list        0.449/s  -100%               --
        
        Score:
        
        array:  0 wallclock secs ( 0.13 usr +  0.02 sys =  0.15 CPU) @ 63725011708.64/s (n=9279572480)
        list:  2 wallclock secs ( 2.21 usr +  0.01 sys =  2.24 CPU) @  0.45/s (n=1)

## DESCRIPTION

nanobench is a tiny benchmarking library.

This library is really tiny. There is no dependencies. Only one file.
You can copy Benchmark.java into your project.


## LICENSE

```
The MIT License (MIT)
Copyright © 2014 Tokuhiro Matsuno, http://64p.org/ <tokuhirom@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
