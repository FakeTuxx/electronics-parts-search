package com.example.electronics.demo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PriceService {

    public List<OnlinePriceInfo> getOctopartPricesByPartNumber(String partNumber) {
        List<OnlinePriceInfo> prices = new ArrayList<>();

        Random random = new Random(partNumber.toUpperCase().hashCode());

        prices.add(new OnlinePriceInfo("DigiKey", randomPrice(random)));
        prices.add(new OnlinePriceInfo("Mouser", randomPrice(random)));
        prices.add(new OnlinePriceInfo("Reichelt", randomPrice(random)));
        prices.add(new OnlinePriceInfo("Farnell", randomPrice(random)));
        prices.add(new OnlinePriceInfo("RS", randomPrice(random)));

        return prices;
    }

    private double randomPrice(Random random) {
        return Math.round((0.02 + (4.50 * random.nextDouble())) * 1000.0) / 1000.0;
    }
}