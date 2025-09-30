# Validex Benchmarks

This module contains performance benchmarks comparing Validex with Java Bean Validation (JSR-303/380).

## Benchmarks

### 1. ValidationComparisonBench
Compares basic validation performance between Validex and Bean Validation:
- Simple user validation with common constraints
- Valid vs invalid object validation
- Null object validation

### 2. ComplexValidationBench
Compares complex validation scenarios with multiple constraints:
- Extended user model with 7 fields
- Multiple validation annotations per field
- Complex regex patterns and size constraints

### 3. ValidateBench (Original)
Original benchmark for Validex-only validation.

## Running Benchmarks

### Prerequisites
- Java 17+
- Maven 3.6+

### Build and Run
```bash
# Build the benchmarks
mvn clean package

# Run all benchmarks
java -jar target/benchmarks-1.0.1-SNAPSHOT.jar

# Run specific benchmark
java -jar target/benchmarks-1.0.1-SNAPSHOT.jar ValidationComparisonBench

# Run with specific parameters
java -jar target/benchmarks-1.0.1-SNAPSHOT.jar -f 1 -wi 3 -i 5 -t 1
```

### Benchmark Parameters
- `-f`: Number of forks (default: 1)
- `-wi`: Warmup iterations (default: 3)
- `-i`: Measurement iterations (default: 5)
- `-t`: Number of threads (default: 1)
- `-bm`: Benchmark mode (Throughput, AverageTime, etc.)

## Expected Results

Validex should demonstrate:
- **Faster validation** due to compile-time code generation
- **Lower memory allocation** compared to reflection-based Bean Validation
- **Better performance** especially for simple validation scenarios

Bean Validation provides:
- **Runtime flexibility** with dynamic constraint configuration
- **Rich constraint library** with extensive built-in validators
- **Industry standard** with wide ecosystem support

## Test Data

### Valid Objects
- Complete user information with valid values
- All constraints satisfied

### Invalid Objects
- Missing required fields
- Values outside allowed ranges
- Invalid formats (email, phone, etc.)
- Negative values where positive required

## Dependencies

- **Validex**: Custom validation framework
- **Bean Validation**: Jakarta Validation API 3.0.2
- **Hibernate Validator**: 8.0.1.Final (reference implementation)
- **JMH**: OpenJDK Microbenchmark Harness 1.37
