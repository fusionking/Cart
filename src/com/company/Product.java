package com.company;

import java.util.Objects;

public class Product {
    private String title;
    private double price;
    private Category category;

    /**
     The default constructor for the ShoppingCart class.
     */
    public Product(String title, double price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    // ** Getters **

    /**
     @return the category attribute of 'this'.
     */
    public Category getCategory() {
        return category;
    }

    /**
     @return the price attribute of 'this'.
     */
    public double getPrice() {
        return price;
    }

    // ** END Getters **

    // ** Overriden Methods **

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(title, product.title) &&
                Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price, category);
    }

    @Override
    public String toString() {
        return "Product {" + title + "}";
    }

    // ** END Overriden Methods **
}
