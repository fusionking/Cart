package com.company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class UtilitiesTest {
    private static Category food;
    private static Category clothes;
    private static Category electronics;

    private static Product apple;
    private static Product banana;
    private static Product jeans;
    private static Product shirt;

    @BeforeAll
    static void initClass(){
        food = new Category("Food");
        clothes = new Category("Clothes");
        electronics = new Category("Electronics");

        apple = new Product("Apple", 15.0, food);
        banana = new Product("Banana", 15.0, food);
        jeans = new Product("Mavi Jeans", 50.0, clothes);
        shirt = new Product("Mavi Shirt", 30.0, clothes);

    }

    @Test
    void distinctByKey() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("Ali");
        names.add("Ayse");
        names.add("John");
        names.add("Jack");
        names.add("Can");
        names.add("Cenk");
        int real = (int) names.stream().filter(Utilities.distinctByKey(n -> n.charAt(0))).count();
        assertEquals(3, real);
    }

    @Test
    void nonexistentCategoryShouldNotBeFound() {
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(apple);
        products.add(shirt);
        boolean isMatching = products.stream().anyMatch(Utilities.isMatchingCategory(electronics));
        assertFalse(isMatching);
    }

    @Test
    void existentCategoryShouldBeFound() {
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(apple);
        products.add(shirt);
        boolean isMatching = products.stream().anyMatch(Utilities.isMatchingCategory(food));
        assertTrue(isMatching);
    }

    @Test
    void filterProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(apple);
        products.add(shirt);
        ArrayList<Product> foundProducts = Utilities.filterProducts(products, Utilities.isMatchingCategory(food));
        assertFalse(foundProducts.isEmpty());
    }

    @Test
    void getRandomElement() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("Ali");
        names.add("Ayse");
        names.add("John");
        String randomName = Utilities.getRandomElement(names, String.class);
        assertTrue(names.contains(randomName));
    }

    @Test
    void countOccurences() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("Ali");
        names.add("Ayse");
        names.add("Ayse");
        names.add("John");

        HashMap<String, Integer> hm = Utilities.countOccurences(names);

        assertEquals(2, hm.get("Ayse"));
    }
}