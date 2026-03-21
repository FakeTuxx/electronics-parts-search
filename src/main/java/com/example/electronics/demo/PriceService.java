package com.example.electronics.demo;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PriceService {

    public List<OnlinePriceInfo> getOctopartPricesByPartNumber(String partNumber) {
        List<OnlinePriceInfo> prices = new ArrayList<>();

        String encoded = URLEncoder.encode(partNumber, StandardCharsets.UTF_8);
        Random random = new Random(partNumber.toUpperCase().hashCode());

        double base = 0.03 + (4.20 * random.nextDouble());

        prices.add(new OnlinePriceInfo(
                "DigiKey",
                realisticVendorPrice(base, 1.08),
                "https://www.digikey.at/en/products/result?keywords=" + encoded
        ));

        prices.add(new OnlinePriceInfo(
                "Mouser",
                realisticVendorPrice(base, 1.04),
                "https://www.mouser.at/c/?q=" + encoded
        ));

        prices.add(new OnlinePriceInfo(
                "Reichelt",
                realisticVendorPrice(base, 0.96),
                "https://www.reichelt.at/at/de/?SEARCH=" + encoded
        ));

        prices.add(new OnlinePriceInfo(
                "Farnell",
                realisticVendorPrice(base, 1.10),
                "https://at.farnell.com/search?st=" + encoded
        ));

        prices.add(new OnlinePriceInfo(
                "RS",
                realisticVendorPrice(base, 1.02),
                "https://at.rs-online.com/web/c/?searchTerm=" + encoded
        ));

        return prices;
    }

    private double realisticVendorPrice(double base, double factor) {
        return Math.round(base * factor * 1000.0) / 1000.0;
    }
}