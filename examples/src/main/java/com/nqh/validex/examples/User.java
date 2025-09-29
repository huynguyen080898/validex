package com.nqh.validex.examples;

import com.nqh.validex.annotations.Min;
import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.annotations.Pattern;
import com.nqh.validex.annotations.Size;

public record User(
        @NotNull @Size(min = 2, max = 40) @Pattern(regex = "^[a-zA-Z\\s]+$", message = "name must contain only letters and spaces") String name,
        @Min(18) int age
) {}


