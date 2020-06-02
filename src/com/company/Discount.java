package com.company;

enum DiscountType{
    RATE, AMOUNT
}

public class Discount {
    // The amount of TL or rate based discount
    private double discountAmount;
    // The type of the discount. Either RATE or AMOUNT.
    private DiscountType discountType;

    /**
     The default constructor for the ShoppingCart class.
     */
    public Discount(double discountAmount, DiscountType discountType) {
        this.discountAmount = discountAmount;
        this.discountType = discountType;
    }

    // ** Getters **

    /**
     @return the discountAmount attribute of 'this'.
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    // ** End Getters **

    /**
     Calculates the TL amount of the discount for the supplied price.

     This should be calculated for rate based discounts.

     For example, if this.discountAmount = 50 and the price is 70, the TL discount amount = 35.

     @param price double the supplied price value
     @return double the TL discount amount
     */
    public double calculateTLDiscountAmount(double price){
        return (discountAmount / 100) * price;
    }

    /**
     Gets the TL amount of the discount for the supplied price, or this.discountAmount as is

     @param price double the supplied price value
     @return the TL discount amount
     */
    public double getTLDiscountAmount(double price){
        return isRateDiscount() ? calculateTLDiscountAmount(price) : getDiscountAmount();
    }

    /**
     Checks whether this is a rate based Discount.

     @return boolean true if this is a rate based Discount.
     */
    public boolean isRateDiscount(){
        return discountType == DiscountType.RATE;
    }

}
