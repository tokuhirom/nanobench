package me.geso.nanobench;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CLI {
    public static void main(String[] args) throws Exception {
        new CLI().run(args);
    }

    public void run(String[] args) throws Exception {
        if (args.length == 0) {
            help();
            return;
        }

        File f = new File(".");
        URL[] cp = {f.toURI().toURL()};
        try (URLClassLoader classLoader = new URLClassLoader(cp,
                ClassLoader.getSystemClassLoader())) {
            Class<?> targetClass = classLoader.loadClass(args[0]);
            Object suite = targetClass.newInstance();
            Benchmark benchmark = new Benchmark(suite);
            benchmark.runByTime(1).timethese().cmpthese();
        }
    }

    public void help() {
        System.out.println("Usage: java -jar nanobench.jar BenchmarkClass");
    }
}
