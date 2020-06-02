package com.company;

public class Coupon extends Discount{
    private double minPurchaseAmount;

    /**
     The default constructor for the ShoppingCart class.
     */
    public Coupon(double discountAmount, DiscountType discountType) {
        super(discountAmount, discountType);
    }

    /**
     The alternative constructor for the ShoppingCart class.
     */
    public Coupon(double minPurchaseAmount, double discountAmount, DiscountType discountType){
        this(discountAmount, discountType);
        this.minPurchaseAmount = minPurchaseAmount;
    }

    /**
     A method which checks whether the Coupon can be applied to cart, based on its minimum cart amount constraint.

     @param cartAmount double the current total price of the products in cart
     @return boolean true if it can be applied
     */
    public boolean isApplicable(double cartAmount) {
        return minPurchaseAmount <= cartAmount;
    }
}
