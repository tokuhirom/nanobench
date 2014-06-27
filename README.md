nanobench
=========

This is a tiny benchmarking library for Java 8.

## SYNOPSIS

```
package me.geso.microbenchmarks;

import me.geso.nanobench.Benchmark;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

import com.google.common.html.HtmlEscapers;

public class HTMLEscape {
	@Test
	public void testHTMLEscape() throws Exception {
		final String src = "<><><><>&&&&;;;;jl2kjlnnfljflksdjfuowu-9urjnl321knl;fu3poifuokbkvnl;uigufjslfjadsipuru1o2krn;lkmfzkjhvojopijkJ:LJKU)!*)($J!KLJOIFHS)JPJ";

		new Benchmark()
				.add("guava", () -> {
					HtmlEscapers.htmlEscaper().escape(src);
				})
				.add("commons", () -> {
					StringEscapeUtils.escapeHtml4(src);
				})
				.add("String.replace",
						() -> {
							src.replace("&", "&amp;").replace(">", "&gt;")
									.replace("<", "&lt;")
									.replace("'", "&apos;")
									.replaceAll("\"", "&quot;");
						}).add("Spring", () -> {
					HtmlUtils.htmlEscape(src);
				}).runByTime(1).timethese().cmpthese();
	}
}
```

Output:
```
Score:

guava:  1 wallclock secs ( 1.14 usr +  0.06 sys =  1.20 CPU) @ 972828.08/s (n=1168278)
commons:  1 wallclock secs ( 1.07 usr +  0.00 sys =  1.08 CPU) @ 39361.53/s (n=42403)
String.replace:  1 wallclock secs ( 1.07 usr +  0.01 sys =  1.08 CPU) @ 61647.92/s (n=66395)
Spring:  1 wallclock secs ( 1.02 usr +  0.00 sys =  1.03 CPU) @ 878340.23/s (n=901452)

Comparison chart:

                      Rate  guava  commons  String.replace  Spring
           guava  972828/s     --    2372%           1478%     11%
         commons   39362/s   -96%       --            -36%    -96%
  String.replace   61648/s   -94%      57%              --    -93%
          Spring  878340/s   -10%    2131%           1325%      --
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

