package me.geso.nanobench;

class ScenarioResult {

    final String title;
    // in nanosec.
    final Score score;

    @Override
    public String toString() {
        return "Result [title=" + title + ", score=" + score + "]";
    }

    ScenarioResult(String title, Score score) {
        this.title = title;
        this.score = score;
    }
}
