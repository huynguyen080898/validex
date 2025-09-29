package com.nqh.validex.benchmarks;

import com.nqh.validex.core.ValidationResult;
import com.nqh.validex.core.Validators;
import org.openjdk.jmh.annotations.*;

public class ValidateBench {

    @State(Scope.Thread)
    public static class ThreadState {
        BenchUser invalid = new BenchUser(null, 17);
        BenchUser valid = new BenchUser("Alice", 30);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public ValidationResult validateInvalid(ThreadState s) {
        return Validators.validate(s.invalid);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public ValidationResult validateValid(ThreadState s) {
        return Validators.validate(s.valid);
    }
}
