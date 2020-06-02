package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testNotEquals() {
        Category food = new Category("Food");
        Product apple = new Product("apple", 5.0, food);
        Product orange = new Product("orange", 5.0, food);
        assertNotEquals(apple, orange);
    }

    @Test
    void testEquals() {
        Category food = new Category("Food");
        Product apple = new Product("apple", 5.0, food);
        Product otherApple = new Product("apple", 5.0, food);
        assertEquals(apple, otherApple);
    }
}