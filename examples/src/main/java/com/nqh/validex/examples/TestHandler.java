package com.nqh.validex.examples;

import com.nqh.validex.core.ValidationResult;
import com.nqh.validex.core.Validators;

public class TestHandler {
    public static void main(String[] args) {
        TestRequest req = new TestRequest(null, 16);
        ValidationResult res = Validators.validate(req);
        System.out.println(res);
    }
}
