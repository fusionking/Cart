package com.company;

import java.util.ArrayList;

public class Campaign extends Discount{

    private Category category;
    private int minItems;

    /**
     The default constructor for the ShoppingCart class.
     */
    public Campaign(double discountAmount, DiscountType discountType) {
        super(discountAmount, discountType);
    }

    /**
     The alternative constructor for the ShoppingCart class.
     */
    public Campaign(Category category, double amount, int minItems, DiscountType discountType){
        this(amount, discountType);
        this.category = category;
        this.minItems = minItems;
    }

    // ** Getters

    /**
     @return the category attribute of 'this'.
     */
    public Category getCategory() {
        return category;
    }

    // ** END Getters

    // ** Instance methods **

    /**
     A method which checks whether the Campaign can be applied to cart, based on its minimum number of product constraint.

     @param numberOfProducts int the current number of products in cart (not distinct)
     @return boolean true if it can be applied
     */
    public boolean isApplicable(int numberOfProducts) {
        return minItems <= numberOfProducts;
    }

    // ** Instance methods **

    // ** Overriden methods **

    @Override
    public String toString() {
        return "Campaign{" +
                "category=" + category +
                ", minItems=" + minItems +
                '}';
    }

    // ** END Overriden methods **

}
