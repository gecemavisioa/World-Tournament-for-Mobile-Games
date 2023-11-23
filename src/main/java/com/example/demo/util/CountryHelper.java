package com.example.demo.util;

import java.util.List;
import java.util.Random;

public class CountryHelper {

    // This class is implemented as helper
    // Each country has a corresponding index according to the list below
    // Helping to avoid switch-cases
    private static List<String> countries = List.of("Turkey", "the United States", "the United Kingdom", "France", "Germany");

    public static String getRandomCountry() {
        int randomIndex = new Random().nextInt(5);
        return countries.get(randomIndex);
    }

    public static int getCountryIndex(String country) {
        return countries.indexOf(country);
    }

    public static String getCountry(int idx) {
        return countries.get(idx);
    }

    public static List<String> getCountryList() {
        return countries;
    }
}
