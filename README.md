# Validex

Lightweight compile-time validation for Java: annotate fields, get generated validators, and validate at runtime with a tiny core.

## Quick start

1) Add annotations to your model

```java
import com.nqh.validex.annotations.*;

public record User(@NotNull @Size(min = 2, max = 40) String name,
                   @Min(18) int age) {}
```

2) Compile to generate validators

```bash
mvn -DskipTests -pl examples -am compile
```

3) Validate at runtime

```java
import com.nqh.validex.core.Validators;
import com.nqh.validex.core.ValidationResult;

ValidationResult result = Validators.validate(new User("A", 17));
if (!result.isValid()) {
  result.violations().forEach(v -> System.out.println(v.path()+": "+v.message()));
}
```

4) Nested validation

```java
public class Order {
  @Valid @NotNull private User customer;
  @Min(1) private int quantity;
  // getters...
}
```

## Modules

- annotations: `@NotNull`, `@Min`, `@Max`, `@Size`, `@Valid`
- processor: annotation processor that generates `<Type>Validator`
- core: runtime helpers: `Validators`, `ValidationResult`, `Violation`
- examples: runnable examples
- benchmarks: JMH performance benchmarks

## Build & run

```bash
# Build all
mvn -DskipTests clean package

# Run example
java -cp examples/target/classes:core/target/classes com.nqh.validex.examples.TestHandler

# Run benchmarks
java -jar benchmarks/target/benchmarks-1.0-SNAPSHOT.jar -wi 3 -i 5 -f 1
```

## Spring integration (optional)

Use `Validators.validateOrThrow(obj)` in controllers/services and map `ValidationException` to 400 responses.

## Configuration

- IDE: enable Annotation Processing
- CLI: `maven-compiler-plugin` is preconfigured in `examples` and `benchmarks`
- Runtime behavior: `Validators.setFailSafe(boolean)` and `Validators.setLogger(...)`

## License

MIT
