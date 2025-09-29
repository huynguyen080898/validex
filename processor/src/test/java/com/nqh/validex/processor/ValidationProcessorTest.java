package com.nqh.validex.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;

class ValidationProcessorTest {

    @Test
    void generatesValidatorForAnnotatedRecord() {
        JavaFileObject source = JavaFileObjects.forSourceString(
                "com.nqh.validex.samples.User",
                "package com.nqh.validex.samples;\n" +
                        "import com.nqh.validex.annotations.*;\n" +
                        "public record User(@NotNull String name, @Min(1) int age) {}\n"
        );

        Compilation compilation = Compiler.javac()
                .withProcessors(new ValidationProcessor())
                .compile(source);

        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("com.nqh.validex.samples.UserValidator")
                .contentsAsUtf8String()
                .contains("class UserValidator");
    }
}

