package com.example.electronics.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartSuggestion {
    private String partNumber;
    private String name;
    private String type;
}