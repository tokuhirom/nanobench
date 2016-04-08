package me.geso.nanobench;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private final List<ScenarioResult> results;

    public Result(List<ScenarioResult> results) {
        this.results = results;
    }

    public Result timethese() {
        System.out.println("\nScore:\n");

        for (ScenarioResult result : results) {
            System.out.println(result.title + ": " + result.score.format());
        }
        return this;
    }

    /**
	 * [ '', 'Rate', 'b', 'a' ], [ 'b', '2885232/s', '--', '-59%' ], [ 'a',
	 * '7099126/s', '146%', '--' ],
	 *
	 * @return
	 */
    public Result cmpthese() {
        System.out.println("\nComparison chart:\n");

        List<List<String>> rows = this.createComparisionTable();
        System.out.print(this.renderTable(rows));
        return this;
    }

    public List<List<String>> createComparisionTable() {
        List<List<String>> rows = new ArrayList<>();
        List<String> headerRow = new ArrayList<>();
        headerRow.add("");
        headerRow.add("Rate");
        for (ScenarioResult result : results) {
            headerRow.add(result.title);
        }
        rows.add(headerRow);

        for (ScenarioResult result : results) {
            List<String> row = new ArrayList<>();
            row.add(result.title);
            row.add(result.score.formatRate());

            for (ScenarioResult col : results) {
                if (col == result) {
                    row.add("--");
                } else {
                    row.add(String.format("%.0f%%",
                            100 * result.score.rate() / col.score.rate()
                                    - 100));
                }
            }
            rows.add(row);
        }
        return rows;
    }

    public String renderTable(List<List<String>> rows) {
        StringBuilder buffer = new StringBuilder();
        List<Integer> colSizes = new ArrayList<>(rows.get(0).size());
        for (int x = 0; x < rows.get(0).size(); ++x) {
            colSizes.add(1); // fill initial values.
        }
        for (int x = 0; x < rows.get(0).size(); ++x) {
            for (int y = 0; y < rows.size(); ++y) {
                List<String> row = rows.get(y);
                String col = row.get(x);
                colSizes.set(x, Math.max(colSizes.get(x), col.length()));
                // Integer currentX = colSizes.get(x);
            }
        }

        for (int y = 0; y < rows.size(); ++y) {
            List<String> row = rows.get(y);
            for (int x = 0; x < row.size(); ++x) {
                buffer.append(String.format("  %" + colSizes.get(x) + "s",
                        row.get(x)));
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
