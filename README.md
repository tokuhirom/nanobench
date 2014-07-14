nanobench
=========

This is a tiny benchmarking library for Java 8.

## SYNOPSIS

Benchmark code(ListBenchmark.java):
```
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListBenchmark {
    // Benchmarking method must be started with 'bench'.
    public void benchArrayList() {
        List<Integer> l = new ArrayList<>();
        for (int i=0; i<1_000_000; ++i) {
            l.add(i);
        }
    }

    public void benchLinkedList() {
        List<Integer> l = new LinkedList<>();
        for (int i=0; i<1_000_000; ++i) {
            l.add(i);
        }
    }
}
```

Command line:
```
> javac ListBenchmark.java
> java -jar nanobench.jar ListBenchmark


Score:

benchArrayList:  1 wallclock secs ( 1.03 usr +  0.10 sys =  1.13 CPU) @ 142.79/s (n=162)
benchLinkedList:  2 wallclock secs ( 1.07 usr +  0.15 sys =  1.21 CPU) @ 146.54/s (n=178)

Comparison chart:

                    Rate  benchArrayList  benchLinkedList
   benchArrayList  143/s              --              -3%
  benchLinkedList  147/s              3%               --
```

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

