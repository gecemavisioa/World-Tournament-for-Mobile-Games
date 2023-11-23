package com.example.demo.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CountryHelperTest {
    private List<String> countries = List.of("Turkey", "the United States", "the United Kingdom", "France", "Germany");

    @Test
    public void GetRandomCountryTest_ReturnCountry() {
        String randomCountry = CountryHelper.getRandomCountry();

        assertTrue(countries.contains(randomCountry));
    }

    @Test
    public void GetCountryIndexTest_ReturnCountryIndex() {
        String country = countries.get(new Random().nextInt(5));

        assertEquals(countries.indexOf(country), CountryHelper.getCountryIndex(country));
    }

    @Test
    public void GetCountryTest_ReturnCountryList() {
        int randomIdx = new Random().nextInt(5);
        String country = CountryHelper.getCountry(randomIdx);

        assertEquals(countries.get(randomIdx), country);
    }

    @Test
    public void testGetCountryList() {
        assertEquals(CountryHelper.getCountryList(), countries);
    }
}
