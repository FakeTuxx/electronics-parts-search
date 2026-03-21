package com.example.electronics.demo;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private List<Part> localParts;
    private List<OnlinePriceInfo> onlinePrices;
    private Double averageOnlinePrice;
    private String message;
}