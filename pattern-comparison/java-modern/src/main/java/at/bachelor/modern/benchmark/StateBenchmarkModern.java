package at.bachelor.modern.benchmark;

import at.bachelor.modern.state.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-Xmx4G"})
public class StateBenchmarkModern {

    @Benchmark
    public void testModernStateWorkflow(Blackhole bh) {
        for (int i = 0; i < 1_000_000; i++) {
            DocumentState doc = new Draft("Title " +i);

            doc = WorkflowService.edit(doc, "My final bachelor's thesis "+i);

            doc = WorkflowService.submitForReview(doc);

            doc = WorkflowService.approve(doc, "Prof. Dr. Mueller", java.time.LocalDate.of(2026,3,31).atStartOfDay());

            bh.consume(doc);
        }
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
            .include(StateBenchmarkModern.class.getSimpleName())
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .result("results-" + StateBenchmarkModern.class.getSimpleName() + ".json")
            .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
