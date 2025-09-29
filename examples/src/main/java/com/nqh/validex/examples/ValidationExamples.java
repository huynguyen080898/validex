package com.nqh.validex.examples;

import com.nqh.validex.core.ValidationResult;
import com.nqh.validex.core.Validators;

/**
 * Demonstrates @Pattern and @Valid validation examples
 */
public class ValidationExamples {

    public static void main(String[] args) {
        System.out.println("=== Validex Pattern & Valid Examples ===\n");

        // Example 1: Valid User
        System.out.println("1. Valid User:");
        User validUser = new User("John Doe", 25);
        ValidationResult userResult = Validators.validate(validUser);
        printResult(userResult);

        // Example 2: Invalid User (pattern violation)
        System.out.println("2. Invalid User (pattern violation):");
        User invalidUser = new User("John123", 25); // contains numbers
        ValidationResult invalidUserResult = Validators.validate(invalidUser);
        printResult(invalidUserResult);

        // Example 3: Valid Account with nested User
        System.out.println("3. Valid Account:");
        Account validAccount = new Account(
            "john_doe",
            "john.doe@example.com",
            "+1234567890",
            validUser
        );
        ValidationResult accountResult = Validators.validate(validAccount);
        printResult(accountResult);

        // Example 4: Invalid Account (multiple violations)
        System.out.println("4. Invalid Account (multiple violations):");
        Account invalidAccount = new Account(
            "jo", // too short
            "invalid-email", // invalid email format
            "123", // invalid phone format
            new User("Jane@#$", 16) // invalid name pattern and age
        );
        ValidationResult invalidAccountResult = Validators.validate(invalidAccount);
        printResult(invalidAccountResult);

        // Example 5: Order with nested validation
        System.out.println("5. Order with nested User validation:");
        Order order = new Order(invalidUser, 0); // invalid user and quantity
        ValidationResult orderResult = Validators.validate(order);
        printResult(orderResult);

        // Example 6: @Size on List/Map/Array
        System.out.println("6. @Size on List/Map/Array:");
        Inventory invOk = new Inventory(
            java.util.List.of("a", "b"),
            java.util.Map.of("k", "v"),
            new int[]{1,2}
        );
        printResult(Validators.validate(invOk));

        Inventory invBad = new Inventory(
            java.util.List.of(), // too small
            java.util.Map.of("k1","v1","k2","v2","k3","v3"), // too many
            new int[]{1} // too short
        );
        printResult(Validators.validate(invBad));
    }

    private static void printResult(ValidationResult result) {
        if (result.isValid()) {
            System.out.println("✓ Valid\n");
        } else {
            System.out.println("✗ Invalid:");
            result.violations().forEach(v -> 
                System.out.println("  - " + v.path() + ": " + v.message() + " (got: " + v.invalidValue() + ")")
            );
            System.out.println();
        }
    }
}
