package me.geso.nanobench;

public class Score {

    long cputime;
    final long usertime;
    final long systemtime;
    final long iters;
    final long real;

    public Score(long real, long cputime, long usertime, long systemtime,
                 long iters) {
        this.real = real;
        this.cputime = cputime;
        this.usertime = usertime;
        this.systemtime = systemtime;
        this.iters = iters;
    }

    public Score add(Score other) {
        return new Score( //
                this.real + other.real, //
                this.cputime + other.cputime, //
                this.usertime + other.usertime, //
                this.systemtime + other.systemtime, //
                this.iters + other.iters);
    }

    public Score diff(Score other) {
        return new Score( //
                Math.max(this.real - other.real, 0), //
                Math.max(this.cputime - other.cputime, 0), //
                Math.max(this.usertime - other.usertime, 0), //
                Math.max(this.systemtime - other.systemtime, 0), //
                this.iters);
    }

    public String format() {
        // timestr
        long n = iters;
        long elapsed = usertime + systemtime;
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(
                "%2d wallclock secs (%5.2f usr + %5.2f sys = %5.2f CPU)",
                (long) (real / 1_000_000_000.0),
                (double) usertime / 1_000_000_000.0,
                (double) systemtime / 1_000_000_000.0,
                (double) cputime / 1_000_000_000.0));
        if (elapsed > 0) {
            builder.append(String.format(" @ %5.2f/s (n=%d)", //
                    (double) n / (elapsed / 1_000_000_000.0), //
                    n));
        }
        return builder.toString();
    }

    public double rate() {
        long elapsed = usertime + systemtime;
        return (double) iters / (elapsed / 1_000_000_000.0);
    }

    public String formatRate() {
        double rate = rate();
        String format = rate >= 100 ? "%.0f" : rate >= 10 ? "%.1f"
                : rate >= 1 ? "%.2f" : rate >= 0.1 ? "%.3f" : "%.2f";
        return String.format(format + "/s", rate);
    }
}
