# Processor Flow

- Init: build handler map (NotNull, Size, Min, Max)
- Process round:
  - For each annotation, collect annotated elements
  - Build set of types to generate
  - For each type:
    - Create `<Type>Validator`
    - Emit imports and class header
    - Emit `validate(T obj)` method with:
      - Null object check
      - Iterate fields, resolve accessor
      - Apply handlers in deterministic order
      - If `@Valid`, validate nested object and prefix violation paths
    - Return `ValidationResult.ok()` or `failure(list)`

Edge cases:
- Unsupported fields (no public field/getter): skipped
- Records: use component accessors

Logging:
- Notes for discovered elements, accessor decisions, generated classes
