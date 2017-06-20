package com.example.demo.entity;

import lombok.Data;

import java.util.List;

@Data
public class RawContact {
    private String name;
    private List<String> phones;
}
