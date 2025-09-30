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
public class ComplexValidationBench {

    private Validator beanValidator;
    
    // Complex Validex test objects
    private ComplexValidexUser validexValidUser;
    private ComplexValidexUser validexInvalidUser;
    
    // Complex Bean Validation test objects
    private ComplexBeanValidationUser beanValidUser;
    private ComplexBeanValidationUser beanInvalidUser;

    @Setup
    public void setup() {
        // Initialize Bean Validation
        ValidatorFactory factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        beanValidator = factory.getValidator();
        
        // Setup complex Validex test data
        validexValidUser = new ComplexValidexUser(
            "John Doe", 
            25, 
            "john.doe@example.com", 
            "+1234567890", 
            85.5,
            "Software Engineer",
            "123 Main St, City, State 12345"
        );
        
        validexInvalidUser = new ComplexValidexUser(
            "A", // Too short name
            16, // Too young
            "invalid-email", // Invalid email
            "123", // Invalid phone
            -5.0, // Negative score
            "", // Empty job title
            "Invalid Address" // Invalid address format
        );
        
        // Setup complex Bean Validation test data
        beanValidUser = new ComplexBeanValidationUser(
            "John Doe", 
            25, 
            "john.doe@example.com", 
            "+1234567890", 
            85.5,
            "Software Engineer",
            "123 Main St, City, State 12345"
        );
        
        beanInvalidUser = new ComplexBeanValidationUser(
            "A", // Too short name
            16, // Too young
            "invalid-email", // Invalid email
            "123", // Invalid phone
            -5.0, // Negative score
            "", // Empty job title
            "Invalid Address" // Invalid address format
        );
    }

    @Benchmark
    public ValidationResult validexComplexValidateValid() {
        return Validators.validate(validexValidUser);
    }

    @Benchmark
    public ValidationResult validexComplexValidateInvalid() {
        return Validators.validate(validexInvalidUser);
    }

    @Benchmark
    public Set<ConstraintViolation<ComplexBeanValidationUser>> beanValidationComplexValidateValid() {
        return beanValidator.validate(beanValidUser);
    }

    @Benchmark
    public Set<ConstraintViolation<ComplexBeanValidationUser>> beanValidationComplexValidateInvalid() {
        return beanValidator.validate(beanInvalidUser);
    }


    // Bean Validation complex user class
    public static class ComplexBeanValidationUser {
        @jakarta.validation.constraints.NotNull(message = "Name cannot be null")
        @jakarta.validation.constraints.NotBlank(message = "Name cannot be blank")
        @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        private String name;
        
        @jakarta.validation.constraints.NotNull(message = "Age cannot be null")
        @jakarta.validation.constraints.Min(value = 18, message = "Age must be at least 18")
        @jakarta.validation.constraints.Max(value = 120, message = "Age must be at most 120")
        private Integer age;
        
        @jakarta.validation.constraints.Email(message = "Email should be valid")
        @jakarta.validation.constraints.NotBlank(message = "Email cannot be blank")
        private String email;
        
        @jakarta.validation.constraints.Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
        private String phoneNumber;
        
        @jakarta.validation.constraints.PositiveOrZero(message = "Score must be positive or zero")
        @jakarta.validation.constraints.Max(value = 100, message = "Score must be at most 100")
        private Double score;
        
        @jakarta.validation.constraints.NotNull(message = "Job title cannot be null")
        @jakarta.validation.constraints.NotBlank(message = "Job title cannot be blank")
        @jakarta.validation.constraints.Size(min = 3, max = 50, message = "Job title must be between 3 and 50 characters")
        private String jobTitle;
        
        @jakarta.validation.constraints.Pattern(regexp = "^\\d+\\s+[A-Za-z\\s]+,\\s*[A-Za-z\\s]+,\\s*[A-Za-z\\s]+\\s+\\d{5}$", 
                 message = "Address must be in format: number street, city, state zipcode")
        private String address;

        public ComplexBeanValidationUser() {}

        public ComplexBeanValidationUser(String name, Integer age, String email, String phoneNumber, 
                                        Double score, String jobTitle, String address) {
            this.name = name;
            this.age = age;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.score = score;
            this.jobTitle = jobTitle;
            this.address = address;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
        public String getJobTitle() { return jobTitle; }
        public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }
}
