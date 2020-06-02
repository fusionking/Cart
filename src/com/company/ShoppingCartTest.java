package com.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.company.Utilities.getRandomElement;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;
    private DeliveryCostCalculator calculator;

    private static Category food;
    private static Category electronics;
    private static Category clothes;
    private static Category cleaning;
    private static Category instruments;
    private static Category brass_instruments;
    private static Category string_instruments;

    private static Product apple;
    private static Product orange;
    private static Product banana;
    private static Product phone;
    private static Product jeans;
    private static Product shirt;
    private static Product wipe;
    private static Product mop;
    private static Product trumpet;
    private static Product sax;
    private static Product violin;

    private static Campaign foodRateCampaign;
    private static Campaign foodAmountCampaign;
    private static Campaign instrumentAmountCampaign;
    private static Campaign brassInstrumentAmountCampaign;

    private static Coupon rateCoupon;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    static void initClass(){
        food = new Category("Food");
        electronics = new Category("Electronics");
        cleaning = new Category("Cleaning");
        clothes = new Category("Clothes");
        instruments = new Category("Instruments");
        brass_instruments = new Category("Brass Instruments", instruments);
        string_instruments = new Category("String Instruments", instruments);

        apple = new Product("Apple", 15.0, food);
        banana = new Product("Banana", 15.0, food);
        orange = new Product("Orange", 10.0, food);
        phone = new Product("Iphone 11", 150.0, electronics);
        jeans = new Product("Mavi Jeans", 50.0, clothes);
        shirt = new Product("Mavi Shirt", 30.0, clothes);
        wipe = new Product("Wipe", 10.0, cleaning);
        mop = new Product("Mop", 15.0, cleaning);
        sax = new Product("Sax", 100.0, brass_instruments);
        trumpet = new Product("Trumpet", 120.0, brass_instruments);
        violin = new Product("Violin", 150.0, string_instruments);

        foodRateCampaign = new Campaign(food, 50.0, 5, DiscountType.RATE);
        foodAmountCampaign = new Campaign(food, 40.0, 5, DiscountType.AMOUNT);
        instrumentAmountCampaign = new Campaign(instruments, 30.0, 2, DiscountType.AMOUNT);
        brassInstrumentAmountCampaign = new Campaign(brass_instruments, 40.0, 2, DiscountType.AMOUNT);

        rateCoupon = new Coupon(100, 10.0, DiscountType.RATE);
    }

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        calculator = cart.getCalculator();

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void getProducts() {
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void getCartAmount() {
        assertEquals(0.0, cart.getCartAmount());
    }

    @Test
    void getCouponTLAmount() {
        assertEquals(0.0, cart.getCouponTLAmount());
    }

    @Test
    void getCampaignTLAmount() {
        assertEquals(0.0, cart.getCampaignTLAmount());
    }

    @Test
    void isCouponApplied() {
        assertFalse(cart.isCouponApplied());
    }

    @Test
    void getDistinctCategoryCount() {
        // Initially, distinct category count must be 0
        assertEquals(0, cart.getDistinctCategoryCount());
        // Add some items to cart with 2 distinct categories
        cart.addItem(orange, 1);
        cart.addItem(apple, 1);
        cart.addItem(phone, 1);
        assertEquals(2, cart.getDistinctCategoryCount());
    }

    @Test
    void getDistinctProductCount() {
        // Initially, distinct product count must be 0
        assertEquals(0, cart.getDistinctProductCount());
        // Add some items to cart with 3 distinct products
        cart.addItem(orange, 5);
        cart.addItem(apple, 3);
        cart.addItem(phone, 2);
        assertEquals(3, cart.getDistinctProductCount());
    }

    @Test
    void getNumberOfProductsInCart() {
        // Initially, the number of products in cart must be 0
        assertEquals(0, cart.getNumberOfProductsInCart());
        // Add 10 different products to the cart
        cart.addItem(orange, 3);
        cart.addItem(banana, 3);
        cart.addItem(phone, 2);
        cart.addItem(mop, 2);
        assertEquals(10, cart.getNumberOfProductsInCart());
    }

    @Test
    void addItem() {
        cart.addItem(apple, 2);
        cart.addItem(orange, 1);
        assertFalse(cart.getProducts().isEmpty());
        assertEquals(3, cart.getNumberOfProductsInCart());
        assertEquals((apple.getPrice() * 2) + orange.getPrice(), cart.getCartAmount());
    }

    @Test
    void addItems() {
        cart.addItems(wipe, shirt);
        assertFalse(cart.getProducts().isEmpty());
        assertEquals(2, cart.getNumberOfProductsInCart());
        assertEquals(wipe.getPrice() + shirt.getPrice(), cart.getCartAmount());
    }

    @Test
    void getDeliveryCost() {
        // Initially, the cart delivery cost should be 0.0
        assertEquals(0.0, cart.getDeliveryCost());
        // Add 5 different products with 2 distinct categories
        cart.addItem(apple, 2);
        cart.addItem(orange, 1);
        cart.addItem(banana, 2);
        cart.addItem(mop, 1);
        cart.addItem(wipe, 2);
        double expected = (calculator.getCostPerDelivery() * 2) + (calculator.getCostPerProduct() * 5);
        expected = Math.round(expected + calculator.getFixedCost());

        assertEquals(expected, cart.getDeliveryCost());
    }

    @Test
    void applyDiscounts() {
        assertThrows(CartIsEmptyException.class, () -> cart.applyDiscounts(foodAmountCampaign));
        assertEquals(0.0, cart.getCampaignTLAmount());
    }

    @Test
    void applyCoupon() {
        assertThrows(CartIsEmptyException.class, () -> cart.applyCoupon(rateCoupon));
        assertEquals(0.0, cart.getCouponTLAmount());
    }

    @Test
    void getCampaignDiscount() {
        assertEquals(0.0, cart.getCampaignDiscount());
    }

    @Test
    void getCouponDiscount() {
        assertEquals(0.0, cart.getCouponDiscount());
    }

    @Test
    void getTotalDiscount() {
        assertEquals(0.0, cart.getTotalDiscount());
    }

    @Test
    void getTotalAmountAfterDiscounts() {
        assertEquals(0.0, cart.getTotalAmountAfterDiscounts());
    }

    @Test
    void appliedCampaignShouldBeMax() {
        // After adding these products, Total cart amount should be 100.0
        cart.addItem(apple, 1);
        cart.addItem(banana, 1);
        cart.addItem(orange, 7);
        // The rate campaign should be the max, because %50 of 100.00 is 50 TL
        // while the amount campaign is a 40 TL discount.
        assertDoesNotThrow(() -> cart.applyDiscounts(foodRateCampaign, foodAmountCampaign));
        // The expected campaign discount should be the food rate campaign
        double expected = foodRateCampaign.getTLDiscountAmount(cart.getCartAmount());

        assertEquals(foodRateCampaign, cart.getMaxCampaign());
        assertEquals(expected, cart.getCampaignDiscount());
    }

    @Test
    void initialCouponUsageShouldDenyCampaignUsage() {
        // After adding these products, Total cart amount should be 100.0
        cart.addItem(apple, 1);
        cart.addItem(banana, 1);
        cart.addItem(orange, 7);

        // Apply a coupon
        assertDoesNotThrow(() -> cart.applyCoupon(rateCoupon));
        assertEquals(cart.getCartAmount() * (rateCoupon.getDiscountAmount() / 100), cart.getCouponDiscount());

        // You cannot apply a campaign after a coupon is applied
        assertThrows(CouponAppliedFirstException.class, () -> cart.applyDiscounts(foodAmountCampaign));
        assertEquals(0.0, cart.getCampaignDiscount());

        // Total discount amount should be 10.0 TL
        assertEquals(10.0, cart.getTotalDiscount());

        // Total amount after discount should be 90.0 TL
        assertEquals(90.0, cart.getTotalAmountAfterDiscounts());
    }

    @Test
    void campaignAndCouponUsage() {
        // After adding these products, Total cart amount should be 100.0
        cart.addItem(apple, 1);
        cart.addItem(banana, 1);
        cart.addItem(orange, 7);

        // Apply the max campaign = foodRateCampaign = 50.0 TL
        assertDoesNotThrow(() -> cart.applyDiscounts(foodRateCampaign, foodAmountCampaign));
        assertEquals(foodRateCampaign.getTLDiscountAmount(cart.getCartAmount()), cart.getCampaignDiscount());

        // Apply a coupon = 10.0 TL
        assertDoesNotThrow(() -> cart.applyCoupon(rateCoupon));
        assertEquals(cart.getCartAmount() * (rateCoupon.getDiscountAmount() / 100), cart.getCouponDiscount());

        // Total discount amount should be 60.0 TL
        assertEquals(60.0, cart.getTotalDiscount());

        // Total amount after discount should be 40.0 TL
        assertEquals(40.0, cart.getTotalAmountAfterDiscounts());

    }

    @Test
    void unapplicableCampaign() {
        // After adding these products, Total cart amount should be 100.0
        cart.addItem(apple, 1);
        cart.addItem(banana, 1);

        // Both campaigns have a minimum 5 product constraint
        assertDoesNotThrow(() -> cart.applyDiscounts(foodRateCampaign, foodAmountCampaign));

        // The campaign TL amount should be 0.0
        assertEquals(0.0, cart.getCampaignDiscount());
    }

    @Test
    void testPrint() {
        cart.addItem(apple, 1);
        cart.addItem(banana, 1);
        cart.addItem(phone, 1);
        cart.addItem(mop, 1);
        cart.addItem(jeans, 1);

        cart.print();

        String out = outContent.toString();
        Product randomProduct = getRandomElement(cart.getProducts(), Product.class);
        Category productCategory = randomProduct.getCategory();

        assertTrue(out.contains(productCategory.toString()));
        assertTrue(out.contains("Total price without discount: " + cart.getCartAmount()));

    }

    @Test
    void getTotalDiscountForCategory() {
        cart.addItem(apple, 2);
        cart.addItem(banana, 3);
        assertDoesNotThrow(() -> cart.applyDiscounts(foodAmountCampaign));

        double expected = foodAmountCampaign.getTLDiscountAmount(cart.getCartAmount());
        assertEquals(expected, cart.getTotalDiscountForCategory(food));
    }

    @Test
    void calculateCategorySubTotal() {
        cart.addItem(apple, 2);
        cart.addItem(banana, 3);
        // The Food category sub total should be the sum of prices of apple * 2 and banana * 3
        double expected = (apple.getPrice() * 2) + (banana.getPrice() * 3);
        assertEquals(expected, cart.calculateCategorySubTotal(food));
        assertEquals(0.0, cart.calculateCategorySubTotal(electronics));
    }

    @Test
    void calculateParentCategorySubTotal() {
        cart.addItem(sax, 1);
        cart.addItem(trumpet, 1);
        // The Instrument category is a parent of Brass Instrument category
        // So, Instrument and Brass Instrument category sub totals should be equal to each other
        double expected = (sax.getPrice() * 1) + (trumpet.getPrice() * 1);
        assertEquals(expected, cart.calculateCategorySubTotal(instruments));
        assertEquals(expected, cart.calculateCategorySubTotal(brass_instruments));
    }

    @Test
    void parentCategoryCampaignShouldBeApplicableForChildCategories() {
        cart.addItem(sax, 1);
        cart.addItem(violin, 1);
        assertDoesNotThrow(() -> cart.applyDiscounts(instrumentAmountCampaign));
        assertEquals(instrumentAmountCampaign.getDiscountAmount(),
                cart.getTotalDiscountForCategory(brass_instruments));
        assertEquals(instrumentAmountCampaign.getDiscountAmount(),
                cart.getTotalDiscountForCategory(string_instruments));
        assertEquals(instrumentAmountCampaign.getDiscountAmount(), cart.getTotalDiscount());

        double expected = cart.getCartAmount() - instrumentAmountCampaign.getDiscountAmount();
        assertEquals(expected, cart.getTotalAmountAfterDiscounts());
    }

    @Test
    void childCategoryCampaignShouldBeApplicableForThatChildCategoryOnly() {
        cart.addItem(sax, 1);
        cart.addItem(violin, 1);
        assertDoesNotThrow(() -> cart.applyDiscounts(brassInstrumentAmountCampaign));
        assertEquals(brassInstrumentAmountCampaign.getDiscountAmount(),
                cart.getTotalDiscountForCategory(brass_instruments));
        assertEquals(0.0, cart.getTotalDiscountForCategory(string_instruments));
        assertEquals(0.0, cart.getTotalDiscountForCategory(instruments));

        assertEquals(brassInstrumentAmountCampaign.getDiscountAmount(), cart.getTotalDiscount());

        double expected = cart.getCartAmount() - brassInstrumentAmountCampaign.getDiscountAmount();
        assertEquals(expected, cart.getTotalAmountAfterDiscounts());
    }

}