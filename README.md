# Validex - Java Compile-Time Validation Library

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.java.net/)
[![Maven Central](https://img.shields.io/maven-central/v/com.nqh/validex.svg)](https://search.maven.org/artifact/com.nqh/validex)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://github.com/huynq/validex/workflows/CI/badge.svg)](https://github.com/huynq/validex/actions)

**Validex** is a lightweight, high-performance Java validation library that provides compile-time validation through annotation processing. Generate type-safe validators at compile time and validate objects at runtime with minimal overhead.

## ✨ Key Features

- 🚀 **Compile-time validation**: Generate validators during compilation for better performance
- 📝 **Annotation-based**: Simple annotations like `@NotNull`, `@Email`, `@Pattern`, `@Size`
- 🎯 **Type-safe**: Full type safety with generated validator classes
- ⚡ **Zero runtime dependencies**: Minimal core with no external dependencies
- 🔧 **IDE friendly**: Works seamlessly with IntelliJ IDEA, Eclipse, and VS Code
- 🏗️ **Maven/Gradle ready**: Easy integration with your build system
- 📊 **Benchmarked**: JMH performance benchmarks included

## 🚀 Quick Start Guide

### Step 1: Add Maven Dependencies

Add Validex to your `pom.xml`:

```xml
<dependency>
    <groupId>com.nqh.validex</groupId>
    <artifactId>annotations</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.nqh.validex</groupId>
    <artifactId>processor</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.nqh.validex</groupId>
    <artifactId>core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Step 2: Annotate Your Model Classes

```java
import com.nqh.validex.annotations.*;

public record User(
    @NotNull 
    @Size(min = 2, max = 40) 
    @Pattern(regex = "^[a-zA-Z\\s]+$", message = "name must contain only letters and spaces") 
    String name,
    
    @Min(18) 
    int age
) {}
```

### Step 3: Compile to Generate Validators

```bash
mvn clean compile
```

The annotation processor automatically generates `UserValidator` class.

### Step 4: Validate Objects at Runtime

```java
import com.nqh.validex.core.Validators;
import com.nqh.validex.core.ValidationResult;

ValidationResult result = Validators.validate(new User("John123", 17));
if (!result.isValid()) {
  result.violations().forEach(v -> 
    System.out.println(v.path() + ": " + v.message() + " (got: " + v.invalidValue() + ")")
  );
}
// Output:
// name: name must contain only letters and spaces (got: John123)
// age: must be >= 18 (got: 17)
```

### Step 5: Nested Object Validation

```java
public class Order {
  @Valid @NotNull private User customer;
  @Min(1) private int quantity;
  // getters...
}
```

## 📦 Available Annotations

Validex provides a comprehensive set of validation annotations:

| Annotation | Description | Example |
|------------|-------------|---------|
| `@NotNull` | Ensures field is not null | `@NotNull String name` |
| `@NotBlank` | Ensures string is not null, empty, or whitespace | `@NotBlank String username` |
| `@NotEmpty` | Ensures collection/array is not empty | `@NotEmpty List<String> tags` |
| `@Size` | Validates collection/string size | `@Size(min=2, max=50) String name` |
| `@Min` | Minimum numeric value | `@Min(18) int age` |
| `@Max` | Maximum numeric value | `@Max(100) int score` |
| `@Pattern` | Regex pattern validation | `@Pattern(regex="^[A-Z]+$") String code` |
| `@Email` | Email format validation | `@Email String email` |
| `@Valid` | Nested object validation | `@Valid User profile` |
| `@Positive` | Positive number validation | `@Positive BigDecimal amount` |
| `@Negative` | Negative number validation | `@Negative BigDecimal debt` |

## 🏗️ Project Structure

Validex is organized into modular components:

- **`annotations`**: Core validation annotations (`@NotNull`, `@Min`, `@Max`, `@Size`, `@Pattern`, `@Valid`, etc.)
- **`processor`**: Annotation processor that generates `<Type>Validator` classes at compile time
- **`core`**: Runtime validation engine with `Validators`, `ValidationResult`, and `Violation` classes
- **`examples`**: Comprehensive examples demonstrating all validation features
- **`benchmarks`**: JMH performance benchmarks comparing Validex with other validation libraries

## 🔧 Development & Testing

### Building from Source

```bash
# Clone the repository
git clone https://github.com/huynq/validex.git
cd validex

# Build all modules
mvn clean package

# Run examples
java -cp examples/target/classes:core/target/classes:annotations/target/classes com.nqh.validex.examples.ValidationExamples

# Run performance benchmarks
java -jar benchmarks/target/benchmarks-1.0-SNAPSHOT.jar -wi 3 -i 5 -f 1
```

### IDE Configuration

**IntelliJ IDEA:**
1. Go to `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`
2. Enable annotation processing
3. Set generated sources directory to `target/generated-sources/annotations`

**Eclipse:**
1. Right-click project → Properties → Java Build Path → Annotation Processing
2. Enable annotation processing
3. Set generated sources directory

## 🌐 Framework Integration

### Spring Boot Integration

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            Validators.validateOrThrow(user);
            // Process valid user
            return ResponseEntity.ok("User created successfully");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getViolations());
        }
    }
}
```

### Custom Error Handling

```java
@ControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", e.getViolations());
        return ResponseEntity.badRequest().body(response);
    }
}
```

## ⚙️ Configuration Options

- **IDE**: Enable Annotation Processing in your IDE settings
- **Maven**: `maven-compiler-plugin` is preconfigured in examples and benchmarks
- **Runtime**: Configure behavior with `Validators.setFailSafe(boolean)` and `Validators.setLogger(...)`

## 📊 Performance

Validex is designed for high performance with compile-time code generation:

- **Zero reflection**: All validation logic is generated at compile time
- **Minimal overhead**: Direct method calls instead of reflection
- **Memory efficient**: No runtime metadata or caching required
- **Fast validation**: Benchmarked against popular validation libraries

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## 🙏 Acknowledgments

- Inspired by Bean Validation (JSR-303) but optimized for compile-time performance
- Built with Java 17+ and modern annotation processing techniques
- Thanks to all contributors and the Java community for feedback and suggestions

---

**⭐ If you find Validex useful, please give it a star on GitHub!**
