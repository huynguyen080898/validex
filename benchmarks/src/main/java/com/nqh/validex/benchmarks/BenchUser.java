package com.nqh.validex.benchmarks;

import com.nqh.validex.annotations.*;

public class BenchUser {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    
    @Min(18)
    @Max(120)
    private int age;
    
    @Email
    @NotBlank
    private String email;
    
    @Pattern(regex = "^\\+?[1-9]\\d{1,14}$")
    private String phoneNumber;
    
    @PositiveOrZero
    private double score;

    public BenchUser() {}

    public BenchUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public BenchUser(String name, int age, String email, String phoneNumber, double score) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}


