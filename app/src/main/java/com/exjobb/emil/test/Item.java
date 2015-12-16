package com.exjobb.emil.test;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Emil on 2015-03-31.
 */
public class Item {

    private String name;
    private BigDecimal price;
    private int picId;
    private String category;
    private boolean bought;

    public Item(String name, BigDecimal price, int picId, String category) {
        this.name = name;
        this.price = price;
        this.picId = picId;
        this.category = category;
        bought = false;
    }

    // Add bank item
    public Item(String category) {
        name = "Blank";
        price = BigDecimal.ZERO;
        picId = R.drawable.placeholder;
        this.category = category;
        bought = false;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPriceString() {
        return NumberFormat.getCurrencyInstance().format(price);
    }

    public int getPicId() {
        return picId;
    }

    public String getCategory() {
        return category;
    }

    public boolean isBought() {
        return bought;
    }

    public void checkBought() {
        bought = true;
    }
}

