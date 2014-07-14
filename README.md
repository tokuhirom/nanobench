nanobench
=========

This is a tiny benchmarking library for Java 8.

## SYNOPSIS

```

package me.geso.microbenchmarks;

import me.geso.nanobench.Benchmark;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

import com.google.common.html.HtmlEscapers;

public class HTMLEscapeBenchmark extends Benchmark {

	final String src = "<><><><>&&&&;;;;jl2kjlnnfljflksdjfuowu-9urjnl321knl;fu3poifuokbkvnl;uigufjslfjadsipuru1o2krn;lkmfzkjhvojopijkJ:LJKU)!*)($J!KLJOIFHS)JPJ";

	public static void main(String[] args) throws Exception {
		new HTMLEscapeBenchmark().runByTime(1).timethese().cmpthese();
	}

	public void benchGuava() {
		HtmlEscapers.htmlEscaper().escape(src);
	}

	public void benchApacheCommons() {
		StringEscapeUtils.escapeHtml4(src);
	}

	public void benchStringReplace() {
		src.replace("&", "&amp;").replace(">", "&gt;")
				.replace("<", "&lt;")
				.replace("'", "&apos;")
				.replaceAll("\"", "&quot;");
	}
}
```

Output:
```

Score:

benchStringReplace:  1 wallclock secs ( 1.06 usr +  0.01 sys =  1.07 CPU) @ 121899.35/s (n=131034)
benchGuava:  1 wallclock secs ( 1.01 usr +  0.00 sys =  1.01 CPU) @ 2416464.32/s (n=2451143)
benchApacheCommons:  1 wallclock secs ( 1.08 usr +  0.00 sys =  1.08 CPU) @ 86865.32/s (n=93920)

Comparison chart:

                           Rate  benchStringReplace  benchGuava  benchApacheCommons
  benchStringReplace   121899/s                  --        -95%                 40%
          benchGuava  2416464/s               1882%          --               2682%
  benchApacheCommons    86865/s                -29%        -96%                  --
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

