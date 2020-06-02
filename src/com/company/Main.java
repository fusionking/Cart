package com.company;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) throws CouponAppliedFirstException, CartIsEmptyException {
        Category food = new Category("Food");
        Category electronics = new Category("Electronics");
        Category cleaning = new Category("Cleaning");
        Category clothes = new Category("Clothes");
        Category instruments = new Category("Instruments");
        Category brassInstruments = new Category("Brass Instruments", instruments);
        Category stringInstruments = new Category("String Instruments", instruments);

        Product apple = new Product("Apple", 15.0, food);
        Product banana = new Product("Banana", 15.0, food);
        Product orange = new Product("Orange", 10.0, food);
        Product phone = new Product("Iphone 11", 150.0, electronics);
        Product jeans = new Product("Mavi Jeans", 50.0, clothes);
        Product wipe = new Product("Wipe", 10.0, cleaning);
        Product trumpet = new Product("Trumpet", 120.0, brassInstruments);
        Product violin = new Product("Violin", 150.0, stringInstruments);

        ShoppingCart cart = new ShoppingCart();
        cart.addItems(apple, banana, orange, phone, jeans);
        cart.addItem(wipe, 5);
        cart.addItem(trumpet, 1);
        cart.addItem(violin, 1);

        System.out.println("The number of products in cart is " + cart.getNumberOfProductsInCart());
        System.out.println("There are " + cart.getDistinctProductCount() + " distinct products in cart");
        System.out.println("There are " + cart.getDistinctCategoryCount() + " distinct categories in cart");

        Campaign rateCampaign = new Campaign(food, 50.0, 5, DiscountType.RATE);
        Campaign amountCampaign = new Campaign(food, 40.0, 15, DiscountType.AMOUNT);
        try {
            cart.applyDiscounts(rateCampaign, amountCampaign);
        } catch (CartIsEmptyException | CouponAppliedFirstException c) {
            System.out.println(ANSI_RED + c.getLocalizedMessage() + ANSI_RESET);
        }
        System.out.println("The total campaign discount TL amount is " + cart.getCampaignTLAmount() + " $");

        Coupon coupon = new Coupon(100, 10.0, DiscountType.RATE);
        try {
            cart.applyCoupon(coupon);
        } catch (CartIsEmptyException c) {
            System.out.println(ANSI_RED + c.getLocalizedMessage() + ANSI_RESET);
        }

        System.out.println("The total coupon discount TL amount is " + cart.getCouponTLAmount() + " $");

        Campaign anotherCampaign = new Campaign(clothes, 5.0, 4, DiscountType.AMOUNT);
        try {
            cart.applyDiscounts(anotherCampaign);
        } catch (CartIsEmptyException | CouponAppliedFirstException c) {
            System.out.println(ANSI_RED + c.getLocalizedMessage() + ANSI_RESET);
        }

        cart.print();

    }
}
