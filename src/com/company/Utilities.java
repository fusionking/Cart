package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utilities {
    // ** Predicates **

    /**
     Finds distinct elements within a Collection based on a key.

     @param keyExtractor the Function which gets the related key, to be used in comparison with other elements
     @return Predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     Checks whether a product's category = given category or is a child of the given category.

     @param category Category a Category instance
     @return Predicate
     */
    public static Predicate<Product> isMatchingCategory(Category category){
        return product -> product.getCategory() == category || product.getCategory().getParent() == category;
    }

    // ** Identity and equalization check methods **

    /**
     Checks whether a Campaign can be applied to a given Category.

     The Campaign can be either applied to a its own category or to a child of its own category.

     @param category Category a Category instance
     @return boolean true if the Campaign can be applied to that Category
     */
    public static boolean isCampaignMatchingCategory(Campaign campaign, Category category){
        return campaign.getCategory() == category || category.getParent() == campaign.getCategory();
    }

    // ** END Identity and equalization check methods **

    // ** END Predicates **

    // ** Filter utility methods **

    /**
     A wrapper method to filter an array of Products, based on a given Predicate.

     @param products ArrayList<Product> an array of Products
     @param predicate Predicate
     @return ArrayList<Product> an array of filtered Products
     */
    public static ArrayList<Product> filterProducts(ArrayList<Product> products, Predicate<Product> predicate){
        return (ArrayList<Product>) products.stream().filter(predicate).collect(Collectors.<Product>toList());
    }

    // ** END Filter Utility methods **


    // ** ArrayList Utility methods **

    /**
     A method which gets a random element from an array of any type, and returns that element.

     @param list ArrayList<T> an array of any Type of objects.
     @param type Class the Class of the objects in the array.
     @return the random Object
     */
    public static <T> T getRandomElement(ArrayList<? super T> list, Class<T> type){
        Random rand = new Random();
        return type.cast(list.get(rand.nextInt(list.size())));
    }

    /**
     A method which counts the number of occurrences of each Object within an array.

     @param list ArrayList<T> an array of any Type of objects.
     @return HashMap<T, Integer> a map of an Object mapped to its occurrence count.

     */
    public static <T> HashMap<T, Integer> countOccurences(ArrayList<? extends T> list){
        HashMap<T, Integer> hm = new HashMap<T, Integer>();

        for (T t : list){
            Integer count = hm.get(t);
            hm.put(t, (count == null) ? 1 : count + 1);
        }

        return hm;
    }

    // ** END ArrayList Utility methods **
}
