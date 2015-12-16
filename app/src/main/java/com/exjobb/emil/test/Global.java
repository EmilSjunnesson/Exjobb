package com.exjobb.emil.test;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Emil on 2015-03-30.
 */
public class Global extends Application {

    private BigDecimal sum = BigDecimal.ZERO;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Item> cart = new ArrayList<Item>();
    private ArrayList<Item> shoppingList = new ArrayList<Item>();
    private ArrayList<Item> blanks = new ArrayList<Item>();
    private int difficulty = 2;

    public void resetStore() {
        cart.clear();
        shoppingList.clear();
        items.clear();
        blanks.clear();
        sum = BigDecimal.ZERO;
        createItems(difficulty);
        createShoppingList(difficulty);
        addBlanksToList();
    }

    private BigDecimal price(double val) {
        return BigDecimal.valueOf(val);
    }

    public BigDecimal getSum() {
        return sum;
    }

    public String getSumString() {
        return NumberFormat.getCurrencyInstance().format(sum.setScale(0, RoundingMode.HALF_UP));
    }

    public void calculateSum() {
        for (int i = 0; i < cart.size(); i++) {
            sum = sum.add(cart.get(i).getPrice());
        }
    }

    //Difficulty 1, 2, 3
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void addToSum(BigDecimal num) {
        this.sum = this.sum.add(num);
    }

    public void removeFromSum(BigDecimal num) {
        this.sum = this.sum.subtract(num);
    }

    public int getCartSize() {
        return cart.size();
    }

    public ArrayList<Item> getItemsFromCategory(String category) {
        ArrayList<Item> toReturn = new ArrayList<Item>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getCategory().equals(category)) {
                toReturn.add(items.get(i));
            }
        }
        return toReturn;
    }

    public boolean addToCart(Item item) {
        //gör check om den finns i listan annars rejecta
        if (inShoppingList(item.getName())) {
            cart.add(item);

            for (int i = 0; i < shoppingList.size(); i++) {
                Log.i("ShoppingList", shoppingList.get(i).getName() + shoppingList.get(i).isBought());
            }

            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Item> getShoppingList() {
        return shoppingList;
    }

    private boolean inShoppingList(String itemName) {
        for (int i = 0; i < shoppingList.size(); i++) {
            if (itemName.equals(shoppingList.get(i).getName())) {
                if (!shoppingList.get(i).isBought()) {
                    shoppingList.get(i).checkBought();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void createItems(int difficulty) {
        //Mejeri
        items.add(new Item("Vispgrädde", price(11.29), R.drawable.vispgradde, "dairy"));
        items.add(new Item("Gräddfil", price(9.75), R.drawable.graddfil, "dairy"));
        items.add(new Item("Mellanmjölk", price(9.55), R.drawable.mellanmjolk, "dairy"));
        items.add(new Item("Crème fraîche", price(23), R.drawable.creme, "dairy"));
        items.add(new Item("Smör", price(30.75), R.drawable.smor, "dairy"));
        items.add(new Item("Filmjölk", price(11.60), R.drawable.fil, "dairy"));
        items.add(new Item("Yoghurt", price(20.02), R.drawable.yoghurt, "dairy"));
        items.add(new Item("Vaniljglass", price(26.65), R.drawable.vglass, "dairy"));
        items.add(new Item("Chokladglass", price(14.32), R.drawable.cglass, "dairy"));
        items.add(new Item("Fetaost", price(32.34), R.drawable.feta, "dairy"));
        items.add(new Item("Hushållsost", price(91.43), R.drawable.husost, "dairy"));
        items.add(new Item("Ädelost", price(21.51), R.drawable.adelost, "dairy"));
        items.add(new Item("Drickyoghurt", price(13.86), R.drawable.drickyoghurt, "dairy"));
        items.add(new Item("Keso", price(8.21), R.drawable.keso, "dairy"));
        items.add(new Item("Flytande margarin", price(12.83), R.drawable.flytmarg, "dairy"));
        Collections.shuffle(items);
        if (difficulty < 2) {
            addBlanks("dairy");
        }
        if (difficulty < 3) {
            addBlanks("dairy");
        }
        //Frukt och grönt
        items.add(new Item("Hallon", price(30.75), R.drawable.hallon, "vegetables"));
        items.add(new Item("Sockerärtor", price(20.48), R.drawable.arta, "vegetables"));
        items.add(new Item("Champinjoner", price(13.30), R.drawable.champ, "vegetables"));
        items.add(new Item("Morötter", price(6.16), R.drawable.morot, "vegetables"));
        items.add(new Item("Ananas", price(20.54), R.drawable.pineapple, "vegetables"));
        items.add(new Item("Spenat", price(24.59), R.drawable.spenat, "vegetables"));
        items.add(new Item("Bananer", price(20.48), R.drawable.banana, "vegetables"));
        items.add(new Item("Gul lök", price(11.24), R.drawable.lok, "vegetables"));
        items.add(new Item("Vitlök", price(22.54), R.drawable.vitlok, "vegetables"));
        items.add(new Item("Citron", price(6.11), R.drawable.citron, "vegetables"));
        items.add(new Item("Lime", price(6.11), R.drawable.lime, "vegetables"));
        items.add(new Item("Äpple", price(15.40), R.drawable.apple, "vegetables"));
        items.add(new Item("Tomat", price(28.70), R.drawable.tomat, "vegetables"));
        items.add(new Item("Blomkål", price(30.75), R.drawable.blomkol, "vegetables"));
        items.add(new Item("Sparris", price(44.10), R.drawable.sparris, "vegetables"));
        Collections.shuffle(items);
        if (difficulty < 2) {
            addBlanks("vegetables");
        }
        if (difficulty < 3) {
            addBlanks("vegetables");
        }
        //Något
        //Något
        //Blanda
        Collections.shuffle(items);
    }

    private void createShoppingList(int difficulty) {
        int listLength = difficulty * 4;
        shoppingList.addAll(items.subList(0, listLength));
    }

    private void addBlanks(String category) {
        for (int i = 0; i < 5; i++) {
            blanks.add(new Item(category));
            for (int j = 0; j <items.size() ; j++) {
                if (items.get(j).getCategory().equals(category)) {
                    items.remove(j);
                    break;
                }
            }
        }
    }

    private void addBlanksToList() {
        items.addAll(blanks);
        Collections.shuffle(items);
    }

    @Override
    public void onCreate() {
        //reinitialize variable
        //resetStore();
    }
}
