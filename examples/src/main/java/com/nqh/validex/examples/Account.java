package com.nqh.validex.examples;

import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.annotations.Pattern;
import com.nqh.validex.annotations.Valid;

/**
 * Example demonstrating @Pattern and @Valid annotations
 */
public class Account {
    @NotNull
    @Pattern(regex = "^[a-zA-Z0-9_]{3,20}$", message = "username must be 3-20 alphanumeric characters or underscore")
    private String username;

    @NotNull
    @Pattern(regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "invalid email format")
    private String email;

    @NotNull
    @Pattern(regex = "^\\+?[1-9]\\d{1,14}$", message = "invalid phone number format")
    private String phoneNumber;

    @Valid
    @NotNull
    private User profile;

    public Account(String username, String email, String phoneNumber, User profile) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User getProfile() {
        return profile;
    }
}
