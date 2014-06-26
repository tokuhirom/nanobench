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

Copyright © 2014 Tokuhiro Matsuno, http://64p.org/ <tokuhirom@gmail.com>

This is free software; you can redistribute it and/or modify it under the same terms as the Perl 5 programming language system itself.

The strategy was inspired from Perl5's Benchmark.pm. It's covered under the Perl 5 License.

## THANKS TO

This library was inspired from Benchmark.pm from Perl5.
http://search.cpan.org/~rjbs/perl-5.20.0/lib/Benchmark.pm

