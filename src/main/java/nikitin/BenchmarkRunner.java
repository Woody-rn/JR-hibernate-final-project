package nikitin;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;

public class BenchmarkRunner {

    public void runBenchmark(String fileNameForResult) {
        createDirIfNotExist(fileNameForResult);
        Options options = new OptionsBuilder()
                .include(BenchmarkDB.class.getSimpleName())
                .forks(1)
                .output(fileNameForResult)
                .build();

        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDirIfNotExist(String fileNameForResult) {
        File outputDir = new File(fileNameForResult);
        File parentFile = outputDir.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }


}
