package com.ezzat.inventoryportal.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String name, pin, id;

    public User(String name, String pin, String id) {
        this.name = name;
        this.pin = pin;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPin() {
        return pin;
    }

    public String getId() {
        return id;
    }
}
