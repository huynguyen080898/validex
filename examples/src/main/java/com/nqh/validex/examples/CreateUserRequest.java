package com.nqh.validex.examples;

import com.nqh.validex.annotations.Min;
import com.nqh.validex.annotations.NotNull;

public class CreateUserRequest {
    @NotNull
    private String username;
    @Min(18)
    private Integer age;

    public CreateUserRequest(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }
}
