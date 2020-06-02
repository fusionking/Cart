package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testNotEquals() {
        Category category = new Category("Food");
        Category otherCategory = new Category("Electronics");
        assertNotEquals(category, otherCategory);
    }

    @Test
    void testEquals() {
        Category category = new Category("Food");
        Category otherCategory = new Category("Food");
        assertEquals(category, otherCategory);
    }
}