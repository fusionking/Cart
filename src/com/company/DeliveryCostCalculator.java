package com.company;

public class DeliveryCostCalculator {

    final private double costPerDelivery;
    final private double costPerProduct;
    private double fixedCost = 2.99;

    /**
     The default constructor for the DeliveryCostCalculator class.
     */
    public DeliveryCostCalculator(double costPerDelivery, double costPerProduct) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    // ** Getters **

    /**
     @return the costPerDelivery attribute of 'this'.
     */
    public double getCostPerDelivery() {
        return costPerDelivery;
    }

    /**
     @return the costPerProduct attribute of 'this'.
     */
    public double getCostPerProduct() {
        return costPerProduct;
    }

    /**
     @return the fixedCost attribute of 'this'.
     */
    public double getFixedCost() {
        return fixedCost;
    }

    // ** END Getters **

    /**
     Gets the number of deliveries based on the number of distinct categories in the supplied cart.

     @param cart ShoppingCart a ShoppingCart instance
     @return int number of deliveries
     */
    private int getNumDeliveries(ShoppingCart cart){
        return cart.getDistinctCategoryCount();
    }

    /**
     Gets the number of products based on the number of distinct products in the supplied cart.

     @param cart ShoppingCart a ShoppingCart instance
     @return int number of products
     */
    private int getNumProducts(ShoppingCart cart){
        return cart.getDistinctProductCount();
    }

    /**
     Calculates the delivery cost for the supplied cart.

     @param cart ShoppingCart a ShoppingCart instance
     @return double the delivery cost
     */
    public double calculateFor(ShoppingCart cart){
        double amount = (costPerDelivery * getNumDeliveries(cart)) + (costPerProduct * getNumProducts(cart));
        return amount == 0.0 ? amount : Math.round(amount + fixedCost);
    }


}
