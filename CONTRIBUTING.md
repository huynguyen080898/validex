## Contributing to Validex

Thank you for considering a contribution! This guide explains how to set up your environment, make changes, and submit pull requests that can be reviewed quickly.

### Code of Conduct

Be respectful and constructive. By participating, you agree to uphold a welcoming environment for everyone.

### Project structure

- `annotations/`: validation annotations
- `processor/`: annotation processor (code generation)
- `core/`: runtime validation utilities
- `examples/`: usage examples
- `benchmarks/`: JMH benchmarks

### Development setup

Requirements:
- Java 17+
- Maven 3.9+

Clone and build:

```bash
git clone https://github.com/huynq/validex.git
cd validex
mvn -DskipTests clean package
```

Run examples:

```bash
java -cp examples/target/classes:core/target/classes:annotations/target/classes com.nqh.validex.examples.ValidationExamples
```

### Testing

Run unit tests across modules:

```bash
mvn test
```

If you add new annotations or handlers, include tests in the relevant module.

### Code style

- Target Java 17
- Prefer descriptive names and early returns
- Keep functions small and focused
- Avoid deep nesting; handle error cases first
- Add concise Javadoc for public APIs

### Commit messages

Use clear, meaningful messages. Conventional Commits are appreciated:

- `feat(processor): add Email regex escaping`
- `fix(core): prevent NPE in Validators`
- `docs: update README quick start`

### Branching and PRs

1. Fork the repo and create a feature branch:
   ```bash
   git checkout -b feat/my-change
   ```
2. Make changes with tests/docs as needed
3. Ensure build is green:
   ```bash
   mvn -DskipTests=false clean verify
   ```
4. Submit a Pull Request to `main` with a concise description:
   - What changed and why
   - Screenshots or logs if helpful
   - Any breaking changes or migration notes

### Adding a new annotation

1. Define the annotation in `annotations/`
2. Implement a handler in `processor/processor/handler/impl/`
3. Register the handler in `ValidationProcessor`
4. Add unit tests and an example usage

### Releasing (maintainers)

Releases are performed via GitHub Actions on tag/release publish and deploy to OSSRH.

Prerequisites (in repository secrets):
- `OSSRH_USERNAME`, `OSSRH_PASSWORD`
- `GPG_PRIVATE_KEY` (base64, ASCII-armored), `GPG_PASSPHRASE`

Create a GitHub release with a semver tag; the workflow signs and deploys artifacts.

### Reporting issues

Please include:
- Version information (commit/tag)
- Steps to reproduce
- Expected vs actual behavior
- Minimal example if possible

### License

By contributing, you agree that your contributions will be licensed under the MIT License.


