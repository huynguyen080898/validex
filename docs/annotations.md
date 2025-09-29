# Annotations Reference

This page documents all validation annotations provided by Validex and how they are handled by the processor/generator.

## Common notes
- All annotations are compile-time processed to generate `<Type>Validator` classes.
- Unless stated otherwise, validations are skipped when the value is `null`. Combine with `@NotNull`/`@NotEmpty`/`@NotBlank` as needed.
- For nested objects, use `@Valid` to trigger validation on the child object and path prefixing.

## Presence and emptiness

### `@NotNull`
- Ensures the value is not `null`.
- Applicable to any type.

### `@NotEmpty`
- Ensures value is not `null` or empty.
- Supported types: `CharSequence` (length > 0), `Collection` (size > 0), `Map` (size > 0), arrays (length > 0).

### `@NotBlank`
- Ensures textual value is not `null` or all-whitespace.
- Supported types: `CharSequence` only.

## Size and length

### `@Size(min, max)`
- Validates that size/length is within `[min, max]`.
- Supported types: `CharSequence.length()`, `Collection.size()`, `Map.size()`, arrays `Array.getLength(...)`.

## Numeric comparisons

### `@Min(value)` / `@Max(value)`
- `@Min`: numeric value must be `>= value`.
- `@Max`: numeric value must be `<= value`.
- Works with any `Number` (boxed primitives and primitive fields).

### `@Positive` / `@PositiveOrZero`
- `@Positive`: numeric value must be `> 0`.
- `@PositiveOrZero`: numeric value must be `>= 0`.

### `@Negative` / `@NegativeOrZero`
- `@Negative`: numeric value must be `< 0`.
- `@NegativeOrZero`: numeric value must be `<= 0`.

## Text and pattern

### `@Pattern(regex, message?)`
- Validates value matches the given regular expression.
- Supported types: `CharSequence` (non-null). Other types are converted with `String.valueOf(value)`.

### `@Email(message?)`
- Validates a string email using a pragmatic regex (not full RFC 5322).
- Supported types: `CharSequence` (non-null). Other types are converted with `String.valueOf(value)`.

## Nested validation

### `@Valid`
- Marker annotation to trigger nested validation of the field’s object.
- Generated validators call `Validators.validate(fieldValue)` and prefix child violation paths with the field name.
- Future extensions (not yet implemented): iterate collections/arrays/maps and validate each element with indexed/keyed path segments.

## Examples

```java
public class Account {
  @NotNull @NotBlank @Pattern(regex = "^[a-zA-Z0-9_]{3,20}$")
  String username;

  @NotNull @Email
  String email;

  @PositiveOrZero
  long balance;

  @Valid @NotNull
  User profile;
}

public record User(@NotNull @Size(min=2,max=40) String name,
                   @Min(18) int age) {}
```

Output violations include field path, human-readable message, and invalid value.
