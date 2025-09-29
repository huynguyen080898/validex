package com.nqh.validex.examples;

import com.nqh.validex.core.ValidationResult;
import com.nqh.validex.core.Validators;

public class TestHandler {
    public static void main(String[] args) {
        User user = new User("A", 17);
        Order order = new Order(user, 0);
        ValidationResult res = Validators.validate(order);
        System.out.println(res);
    }
}
