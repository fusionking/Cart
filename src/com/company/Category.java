package com.company;

import java.util.Objects;

public class Category {
    private String title;
    private Category parent = null;

    /**
     The default constructor for the ShoppingCart class.
     */
    public Category(String title, Category parent) {
        this.title = title;
        this.parent = parent;
    }

    /**
     The alternative constructor for the ShoppingCart class.
     */
    public Category(String title){
        this(title, null);
    }

    // ** Getters **

    /**
     @return the title attribute of 'this'.
     */
    public String getTitle() {
        return title;
    }

    /**
     @return the parent attribute of 'this'.
     */
    public Category getParent() {
        return parent;
    }

    // ** END Getters **

    // ** Overriden Methods **

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(title, category.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Category {" + title + "}";
    }

    // ** END Overriden Methods **

}
