package com.thonners.kooku;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

/**
 * Class to hold individual menu items and their relevant details.
 *
 * @author M Thomas
 * @since 22/05/16
 */
public class ChefMenuItem implements Parcelable {

    private int itemID;
    private String title;
    private String subtitle;
    private String description;
    private double price;
    private ArrayList<String> ingredients = new ArrayList<>();
    private ItemAwardsManager.ItemAwards awards ;

    private boolean isVegetarian = false ;
    private boolean isVegan = false ;
    private boolean isOrganic = false ;
    private boolean containsNuts = false ;
    private boolean containsAlcohol = false ;
    private boolean containsLactose = false ;

    /**
     * Menu item constructor.
     *
     * @param title       Name of the dish
     * @param subtitle    Accompaniments
     * @param description Description of the dish
     * @param price       Price of the dish
     * @param ingredients List of ingredients in the dish.
     */
    public ChefMenuItem(int itemID, String title, String subtitle, String description, double price, ArrayList<String> ingredients, boolean isVegetarian, boolean isVegan, boolean isOrganic, boolean containsNuts, boolean containsAlcohol, boolean containsLactose) {
        this.itemID = itemID;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.awards = new ItemAwardsManager.ItemAwards(isVegetarian,isVegan,isOrganic) ;
        this.containsNuts = containsNuts;
        this.containsAlcohol = containsAlcohol;
        this.containsLactose = containsLactose ;
    }

    // Getter methods

    public int getItemID() {
        return itemID;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public ItemAwardsManager.ItemAwards getAwards() {
        return awards;
    }

    /**
     * Method to return a string with the appropriate currency, and the price formatted to 1d.p.
     * @return
     */
    public String getPriceString() {
        Currency currency = Currency.getInstance(Locale.getDefault());
        String currencySymbol = currency.getSymbol() ;
        return String.format(currencySymbol + "%1$.1f",price) ;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getIngredientsString() {
        // Initialise string to be returned
        String ing = "Ingredients:\n" ;
        for (String ingredient : ingredients) {
            ing += ingredient + ", " ;
        }
        // Return a substring 2 characters shorter than the full length to remove the trailing ", " from the final ingredient
        return ing.substring(0,ing.length() - 2) + ".";
    }

    /**
     * Returns a string detailing whether the item contains nuts, alcohol, etc.
     * @param context The activity context - used to get the String resources
     * @return A string with details of what the item contains
     */
    public String getContains(Context context) {
        String contains = "" ;
        if (containsNuts) {
            contains += context.getString(R.string.contains_nuts) + "\n";
        }
        if (containsAlcohol) {
            contains += context.getString(R.string.contains_alcohol) + "\n";
        }
        if (containsLactose) {
            contains += context.getString(R.string.contains_lactose) + "\n";
        }

        return contains ;
    }

    // Parcelable Stuff

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeByte((byte) (isVegetarian ? 1 : 0));     //if isVegetarian == true, byte == 1
        dest.writeByte((byte) (isVegan ? 1 : 0));
        dest.writeByte((byte) (isOrganic ? 1 : 0));
        dest.writeByte((byte) (containsNuts ? 1 : 0));
        dest.writeByte((byte) (containsAlcohol ? 1 : 0));
        dest.writeList(ingredients);
        dest.writeParcelable(awards, flags);
    }

    public static final Creator<ChefMenuItem> CREATOR
            = new Creator<ChefMenuItem>() {
        public ChefMenuItem createFromParcel(Parcel in) {
            return new ChefMenuItem(in);
        }

        public ChefMenuItem[] newArray(int size) {
            return new ChefMenuItem[size];
        }
    };

    private ChefMenuItem(Parcel in) {
        itemID = in.readInt();
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
        price = in.readDouble() ;
        isVegetarian = in.readByte() != 0;     //isVegetarian == true if byte != 0
        isVegan = in.readByte() != 0;
        isOrganic = in.readByte() != 0;
        containsNuts = in.readByte() != 0;
        containsAlcohol = in.readByte() != 0;
        ingredients = new ArrayList<>() ;
        in.readList(ingredients, String.class.getClassLoader());
        awards = in.readParcelable(ItemAwardsManager.ItemAwards.class.getClassLoader()) ;
    }
}
