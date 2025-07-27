package com.example.demo.model;


import lombok.Data;

@Data
public class user {
    private int id;
    private String name;

    public user(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
