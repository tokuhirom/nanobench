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
