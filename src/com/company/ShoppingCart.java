package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.company.Utilities.*;

public class ShoppingCart {
    // The array of Product's in cart.
    private ArrayList<Product> products;
    // The campaign applied with the maximum price.
    private Campaign maxCampaign;
    // The DeliveryCostCalculator instance attribute which helps to calculate the delivery cost.
    private DeliveryCostCalculator calculator = new DeliveryCostCalculator(4.0, 2.0);

    // The total price of the products in cart.
    private double cartAmount = 0.0;
    // The total price of the coupon/s applied.
    private double couponTLAmount = 0.0;
    // The total price of the campaign/s applied.
    private double campaignTLAmount = 0.0;
    // true if the coupon is applied.
    private boolean isCouponApplied = false;

    // ** Default Constructor **

    /**
     The default constructor for the ShoppingCart class.
     */
    public ShoppingCart() {
        products = new ArrayList<Product>();
    }

    // ** Getters **

    /**
     @return the products attribute of 'this'.
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     @return the cartAmount attribute of 'this'.
     */
    public double getCartAmount() {
        return cartAmount;
    }

    /**
     @return the couponTLAmount attribute of 'this'.
     */
    public double getCouponTLAmount() {
        return couponTLAmount;
    }

    /**
     @return the campaignTLAmount attribute of 'this'.
     */
    public double getCampaignTLAmount() {
        return campaignTLAmount;
    }

    /**
     @return the isCouponApplied attribute of 'this'.
     */
    public boolean isCouponApplied() {
        return isCouponApplied;
    }

    /**
     @return the calculator attribute of 'this'.
     */
    public DeliveryCostCalculator getCalculator() {
        return calculator;
    }

    /**
     @return the maximum priced campaign attribute of 'this'.
     */
    public Campaign getMaxCampaign() {
        return maxCampaign;
    }

    // ** END Getters **

    // ** Setters **

    /**
     Sets the isCouponApplied attribute of 'this' to the supplied boolean value.
     @param couponApplied boolean true if the coupon is applied, otherwise false
     */
    public void setCouponApplied(boolean couponApplied) {
        isCouponApplied = couponApplied;
    }

    /**
     Sets the maxCampaign attribute of 'this' to the supplied Campaign value.
     @param maxCampaign Campaign the campaign with the maximum price
     */
    public void setMaxCampaign(Campaign maxCampaign) {
        this.maxCampaign = maxCampaign;
    }

    // ** END Setters **

    // ** Methods for calculating cart product related statistics **

    /**
     Finds the number of distinct categories in cart by inspecting each product category in the cart.
     @return int the number of distinct categories in cart
     */
    public int getDistinctCategoryCount(){
        return (int) products.stream().filter(distinctByKey(p -> p.getCategory().getTitle())).count();
    }

    /**
     Finds the number of distinct products in cart by making use of Product's equals method.
     @return int the number of distinct products in cart
     */
    public int getDistinctProductCount(){
        return (int) products.stream().filter(distinctByKey(p -> p)).count();
    }

    /**
     Finds the total number of products in cart.
     @return int total number of products in cart
     */
    public int getNumberOfProductsInCart(){
        return products.size();
    }

    // ** END Methods for calculating cart product related statistics **

    // ** Methods for adding products to the cart **

    /**
     Adds multiple products with quantity set to 1 to our cart.
     Adds each product instance to this.products
     @param products Product multiple or a single Product instance.
     */
    public void addItems(Product... products){
        for (Product product : products){
            addItem(product, 1);
        }
    }

    /**
     Adds a single product with a specified quantity to our cart.
     Adds the product instance (quantity) times to this.products.
     @param product Product a single Product instance.
     @param quantity int the number of the product
     */
    public void addItem(Product product, int quantity){
        for (int i = 0; i < quantity; i++){
            products.add(product);
            cartAmount += product.getPrice();
        }
    }

    // ** END Methods for adding products to the cart **

    // ** Methods to calculate the delivery cost **

    /**
     Calls this.calculator's calculateFor method to calculate the delivery cost.
     @return double The delivery cost
     */
    public double getDeliveryCost(){
        return calculator.calculateFor(this);
    }

    // ** END Methods to calculate the delivery cost **

    // ** Methods to apply campaign discounts **

    /**
     Applies the supplied campaign discounts to the cart.
     For each campaign, checks whether the campaign suits the constraints.
     Finds the maximum priced campaign among the campaigns and updates this.campaignTLAmount with the discount amount.

     @param campaigns Campaign multiple Campaign instances
     @throws CouponAppliedFirstException if a Coupon is applied to cart before
     @throws CartIsEmptyException if there are no products in cart
     */
    public void applyDiscounts(Campaign... campaigns) throws CouponAppliedFirstException, CartIsEmptyException {
        System.out.println("Applying campaigns...");

        if (products.isEmpty()){
            throw new CartIsEmptyException("The cart is empty without any products.");
        }

        if (isCouponApplied()) {
            throw new CouponAppliedFirstException("There is a coupon applied first, you cannot apply a campaign.");
        }

        ArrayList<Campaign> applicableCampaigns = findApplicableCampaigns(campaigns);
        if (!applicableCampaigns.isEmpty()) {
            Campaign maxCampaign = findMaxCampaign(applicableCampaigns);
            double maxTLDiscountAmount = findCampaignTLDiscountAmount(maxCampaign);
            campaignTLAmount += maxTLDiscountAmount;
        }

    }

    /**
     Finds the maximum priced campaign among the campaigns.
     If the Campaign is rate based, calculates the TL equivalent of the Campaign's Category subtotal.
     Else, just compares its discount amount.

     Sets this.maxCampaign to the found Campaign to keep state.

     @param campaigns ArrayList<Campaign> an array of Campaign instances
     @return Campaign the Campaign with the maximum price (discount price)
     */
    private Campaign findMaxCampaign(ArrayList<Campaign> campaigns){
        double maxTLDiscountAmount = 0.0;
        Campaign maxCampaign = null;

        if (campaigns.size() == 1) {
            maxCampaign = campaigns.get(0);
        } else {
            for (Campaign campaign : campaigns){
                double discountAmount = findCampaignTLDiscountAmount(campaign);
                if (discountAmount >= maxTLDiscountAmount){
                    maxTLDiscountAmount = discountAmount;
                    maxCampaign = campaign;
                }
            }
        }

        setMaxCampaign(maxCampaign);
        return maxCampaign;
    }

    /**
     Finds the TL equivalent of the Campaign's Category subtotal price.
     If the Campaign is rate based, calculates the above value.
     Else, just returns the Campaign's discount amount.

     @param campaign Campaign a Campaign instance
     @return double the TL equivalent of the Campaign discount
     */
    private double findCampaignTLDiscountAmount(Campaign campaign){
        double categorySubTotal = calculateCategorySubTotal(campaign.getCategory());
        return campaign.getTLDiscountAmount(categorySubTotal);
    }

    /**
     Checks each Campaign against their constraints and decides whether the current shopping cart fits these constraints.
     For example, checks if the cart has 5 or more products for Campaign with 5 minimum product constraint

     @param campaigns Campaign multiple or a single Campaign instance
     @return ArrayList<Campaign> array of applicable Campaigns whose constraints are met.
     */
    private ArrayList<Campaign> findApplicableCampaigns(Campaign... campaigns){
        ArrayList<Campaign> applicableCampaigns = new ArrayList<Campaign>();
        for (Campaign campaign : campaigns){
            boolean isMinProductsSatisfied = campaign.isApplicable(getNumberOfProductsInCart());
            boolean isCategorySatisfied = isContainingProductWithCategory(campaign.getCategory());
            if (isMinProductsSatisfied && isCategorySatisfied){
                System.out.println("The category and minimum products are satisfied for campaign " + campaign);
                applicableCampaigns.add(campaign);
            } else {
                System.out.println("There are either not enough products in cart or there is no category for " + campaign);
            }
        }
        return applicableCampaigns;
    }
    // ** END Methods to apply campaign discounts

    // ** Methods to apply coupon discounts **

    /**
     Applies a Coupon to the current shopping cart.
     Updates this.couponTLAmount by the Coupon's discount amount in TL.

     @param coupon Coupon a Coupon instance
     @throws CartIsEmptyException if there are no products in cart
     */
    public void applyCoupon(Coupon coupon) throws CartIsEmptyException {
        if (products.isEmpty()){
            throw new CartIsEmptyException("The cart is empty without any products.");
        }
        if (coupon.isApplicable(cartAmount)){
            // Returns either the TL amount of the coupon rate, applied to the total cart amount
            // or just the discount amount as TL as is
            couponTLAmount += coupon.getTLDiscountAmount(cartAmount);
            setCouponApplied(true);
        }
    }
    // ** END Methods to apply coupon discounts **

    // ** Methods related to Categories **

    /**
     Checks if there are any products in cart with the category supplied.
     If the category argument is a parent Category, checks if there are products with
     child categories belonging to this parent.

     @param category Category a Category instance
     @return boolean true if there are any products with category = supplied category or category as their parent.
     */
    private boolean isContainingProductWithCategory(Category category){
        return products.stream().anyMatch(isMatchingCategory(category));
    }

    /**
     Finds products in cart with category as the supplied category.
     If the category argument is a parent Category, checks if there are products with
     child categories belonging to this parent.

     @param category Category a Category instance
     @return ArrayList<Product> an array of Products with category = supplied category or category as their parent.
     */
    private ArrayList<Product> findProductsWithCategory(Category category){
        return filterProducts(products, isMatchingCategory(category));
    }

    /**
     Calculates a Category subtotal price based on the products in cart.

     @param category Category a Category instance
     @return double the Category subtotal price
     */
    public double calculateCategorySubTotal(Category category){
        ArrayList<Product> productsWithCategory = findProductsWithCategory(category);
        double categorySubTotal = 0.0;
        for (Product product : productsWithCategory){
            categorySubTotal += product.getPrice();
        }
        return categorySubTotal;
    }

    /**
     Groups the products in cart based on their Category.

     Note that if a Product's Category is a child of a parent Category,
     the calculated HashMap's key will be the parent Category.

     For example, a "trumpet" belongs to the Brass Instruments subcategory with Instruments as its parent.
     Example HashMap:

     HashMap(key=Category("Instruments"), value={Product("trumpet")})

     @return HashMap<Category, ArrayList<Product>> a map of Categories mapped to an array of Products with that Category
     */
    private HashMap<Category, ArrayList<Product>> groupProductsByCategory(){
        HashMap<Category, ArrayList<Product>> map = new HashMap<Category, ArrayList<Product>>();
        for (Product product : products){
            Category category = product.getCategory();
            Category key = category.getParent() != null ? category.getParent() : category;
            if (!map.containsKey(key)){
                ArrayList<Product> groupedProducts = new ArrayList<Product>();
                groupedProducts.add(product);
                map.put(key, groupedProducts);
            } else {
                map.get(key).add(product);
            }
        }
        return map;
    }

    // ** END Methods related to Categories **

    // ** Methods to get Discounts **

    /**
     Gets the total Campaign discount amount in TL.

     @return double the Campaign discount amount in TL.
     */
    public double getCampaignDiscount(){
        return campaignTLAmount;
    }

    /**
     Gets the total Coupon discount amount in TL.

     @return double the Coupon discount amount in TL.
     */
    public double getCouponDiscount(){
        return couponTLAmount;
    }

    /**
     Calculates the total discount amount in TL for the Campaign + Coupon combined.

     @return double the Campaign + Coupon discount amounts in TL.
     */
    public double getTotalDiscount(){
        return getCampaignDiscount() + getCouponDiscount();
    }

    /**
     Gets the discount amount for the Category supplied.

     Note that if this.maxCampaign's Category = parent of the Category supplied,
     the discount amount is calculated and returned as well.

     If this.maxCampaign's Category does not match the supplied Category, 0.0 is returned.

     @param category Category instance.
     @return double the discount amount for the Category in TL.
     */
    public double getTotalDiscountForCategory(Category category){
        if (maxCampaign == null) {
            return 0.0;
        }

        if (isCampaignMatchingCategory(maxCampaign, category)) {
            return findCampaignTLDiscountAmount(maxCampaign);
        } else {
            return 0.0;
        }
    }

    // ** END Methods to get Discounts

    // ** Methods to get total amount after discounts **

    /**
     Calculates the total cart amount after discounts are applied.

     If the total discount is greater than or equal to the total cart amount, 0.0 is returned.

     @return double the total cart amount after the discounts are applied.
     */
    public double getTotalAmountAfterDiscounts(){
        double totalDiscount = getTotalDiscount();
        if (totalDiscount >= cartAmount) {
            return 0.0;
        } else {
            return cartAmount - totalDiscount;
        }
    }
    // ** END Methods to get total amount after discounts **

    // ** Print methods **

    /**
     A method which prints cart related statistics to the standard output.

     Prints out products in cart, grouped under their categories.
     Prints out each product's title, quantity and unit price.
     Prints out the category subtotal, and the category discount amount.
     Prints out the total price without discount, total price with discount, the total discount and the delivery cost
     */
    public void print(){
        System.out.println("Your cart looks like this: \n");

        HashMap<Category, ArrayList<Product>> map = groupProductsByCategory();
        for (Category category: map.keySet()){
            String categoryName = category.toString();
            System.out.println("=== " + categoryName + " ===\n");

            ArrayList<Product> products = map.get(category);
            HashMap<Product, Integer> occurrenceMap = countOccurences(products);

            for (Map.Entry<Product, Integer> entry: occurrenceMap.entrySet()){
                Product product = entry.getKey();
                int quantity = entry.getValue();
                if (product.getCategory().getParent() == category) {
                    System.out.println("\t === Sub " + product.getCategory().toString() + " === \n");
                    System.out.println("\t \t " + product.toString() +
                            " Quantity: " + quantity + " Unit Price: " + product.getPrice() + " $" +" \n"
                    );
                } else {
                    System.out.println("\t " + product.toString() +
                            " Quantity: " + quantity + " Unit Price: " + product.getPrice() + " $" +" \n"
                    );
                }
            }
            System.out.println("\t Category Subtotal: " + calculateCategorySubTotal(category) + " $\n ");
            System.out.println("\t Category Discount Amount: " + getTotalDiscountForCategory(category) + " $");
            System.out.println("\n");
        }

        System.out.println("Total price without discount: " + getCartAmount() + " $\n");
        System.out.println("Total discount amount: " + getTotalDiscount() + " $\n");
        System.out.println("Total price with discount: " + getTotalAmountAfterDiscounts() + " $\n");
        System.out.println("Total delivery cost: " + getDeliveryCost() + " $\n");
    }

    // ** END Print methods


}
