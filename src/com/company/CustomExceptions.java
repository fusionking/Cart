package com.company;

/**
 CouponAppliedFirstException is an Exception class which is thrown
 if the code tries to apply a Campaign on a cart, which has a Coupon applied before.
 */
class CouponAppliedFirstException extends Exception {
    public CouponAppliedFirstException(String message) {
        super(message);
    }
}

/**
 CouponAppliedFirstException is an Exception class which is thrown
 if there are no products in cart.
 */
class CartIsEmptyException extends Exception {

    public CartIsEmptyException(String message) {
        super(message);
    }
}
