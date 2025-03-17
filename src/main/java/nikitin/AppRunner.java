package nikitin;

import nikitin.presentation.View;

public class AppRunner {

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();
        String fileNameForResult = "output/benchmark_results.txt";
        runner.runBenchmark(fileNameForResult);

        if(args.length > 0 && args[0].equals("-view")) {
            View view = new View();
            view.showResult(fileNameForResult);
        }
    }
}