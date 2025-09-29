package com.nqh.validex.examples;

import com.nqh.validex.annotations.Min;
import com.nqh.validex.annotations.NotNull;
import com.nqh.validex.annotations.Valid;

public class Order {
    @Valid
    @NotNull
    private User customer;

    @Min(1)
    private int quantity;

    public Order(User customer, int quantity) {
        this.customer = customer;
        this.quantity = quantity;
    }

    public User getCustomer() {
        return customer;
    }

    public int getQuantity() {
        return quantity;
    }
}


