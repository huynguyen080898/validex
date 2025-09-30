package com.nqh.validex.benchmarks;

import com.nqh.validex.core.ValidationResult;
import com.nqh.validex.core.Validators;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.openjdk.jmh.annotations.*;

import java.util.Set;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(java.util.concurrent.TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class ValidationComparisonBench {

    private Validator beanValidator;
    
    // Validex test objects
    private BenchUser validexValidUser;
    private BenchUser validexInvalidUser;
    
    // Bean Validation test objects
    private BeanValidationUser beanValidUser;
    private BeanValidationUser beanInvalidUser;

    @Setup
    public void setup() {
        // Initialize Bean Validation
        ValidatorFactory factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        beanValidator = factory.getValidator();
        
        // Setup Validex test data
        validexValidUser = new BenchUser("Alice Johnson", 30, "alice@example.com", "+1234567890", 95.5);
        validexInvalidUser = new BenchUser("", 17, "invalid-email", "123", -10.0);
        
        // Setup Bean Validation test data
        beanValidUser = new BeanValidationUser(
            "Alice Johnson", 
            30, 
            "alice@example.com", 
            "+1234567890", 
            95.5
        );
        
        beanInvalidUser = new BeanValidationUser(
            "", // Invalid: blank name
            17, // Invalid: too young
            "invalid-email", // Invalid: not a valid email
            "123", // Invalid: phone number format
            -10.0 // Invalid: negative score
        );
    }

    @Benchmark
    public ValidationResult validexValidateValid() {
        return Validators.validate(validexValidUser);
    }

    @Benchmark
    public ValidationResult validexValidateInvalid() {
        return Validators.validate(validexInvalidUser);
    }

    @Benchmark
    public Set<ConstraintViolation<BeanValidationUser>> beanValidationValidateValid() {
        return beanValidator.validate(beanValidUser);
    }

    @Benchmark
    public Set<ConstraintViolation<BeanValidationUser>> beanValidationValidateInvalid() {
        return beanValidator.validate(beanInvalidUser);
    }

    // Additional benchmarks for different validation scenarios
    
    @Benchmark
    public ValidationResult validexValidateNull() {
        return Validators.validate(null);
    }
}
