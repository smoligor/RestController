package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class RawContact {
    private String name;
    private List<String> phones;
}
