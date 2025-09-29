package com.nqh.validex.benchmarks;

import com.nqh.validex.annotations.Min;
import com.nqh.validex.annotations.NotNull;

public class BenchUser {
    @NotNull
    private String name;
    @Min(18)
    private int age;

    public BenchUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}


