package at.bachelor.modern.benchmark;


import at.bachelor.modern.strategy.DiscountService;
import at.bachelor.modern.strategy.DiscountStrategy;
import at.bachelor.modern.strategy.PercentageDiscount;
import at.bachelor.modern.strategy.ShoppingCart;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-Xmx4G"})
public class StrategyBenchmarkModern {

    private List<ShoppingCart> carts;
    private DiscountStrategy strategy;

    @Setup(Level.Trial)
    public void setup() {
        carts = new ArrayList<>(10_000_000);
        strategy = new PercentageDiscount(0.20);

        for (int i = 0; i < 10_000_000; i++) {
            double value = (i * 137) % 2000;
            carts.add(new ShoppingCart(value, strategy));
        }
    }

    @Benchmark
    public double testModernStreamPipeline() {
        return carts.stream()
            .map(DiscountService::calculateFinalPrice)
            .filter(price -> price.finalPrice() > 1000.0)
            .limit(5)
            .mapToDouble(DiscountService.CheckoutResult::finalPrice)
            .sum();
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
            .include(StrategyBenchmarkModern.class.getSimpleName())
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .result("results-" + StrategyBenchmarkModern.class.getSimpleName() + ".json")
            .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
