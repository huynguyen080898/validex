package com.nqh.validex.benchmarks;

import com.nqh.validex.annotations.*;

public class ComplexValidexUser {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
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
    @Max(100)
    private double score;
    
    @NotNull
    @NotBlank
    @Size(min = 3, max = 50)
    private String jobTitle;
    
    @Pattern(regex = "^\\d+\\s+[A-Za-z\\s]+,\\s*[A-Za-z\\s]+,\\s*[A-Za-z\\s]+\\s+\\d{5}$")
    private String address;

    public ComplexValidexUser() {}

    public ComplexValidexUser(String name, int age, String email, String phoneNumber, 
                             double score, String jobTitle, String address) {
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
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
