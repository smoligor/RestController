package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Contact {
    private String name;
    private String phones;
}
