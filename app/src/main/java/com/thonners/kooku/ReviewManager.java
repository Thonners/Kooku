package com.thonners.kooku;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to manage submitting and obtaining reviews of dishes
 *
 * @author M Thomas
 * @since 07/06/16
 */

public class ReviewManager {

    private static final String LOG_TAG = "ReviewManager" ;

    private HashMap<Integer, ArrayList<Review>> reviews = new HashMap<>() ;

    public ReviewManager() {

    }

    /**
     * Method to submit a rating and review to the server
     * @param itemID    The ID of the item being reviewed
     * @param rating    The rating (out of 5)
     * @param review    The review
     */
    public void submitReview(int itemID, int rating, String review) {
        // Submit the review to the server
        Log.d(LOG_TAG,"Submitting review for itemID: " + itemID + ". Rating = " + rating + ", Review = " + review);
        // If it's the first review, create the array list
        if (!reviews.containsKey(itemID)) reviews.put(itemID, new ArrayList<Review>());
        // Add the review to the ArrayList
        ArrayList<Review> reviewList = reviews.get(itemID) ;
        reviewList.add(new Review(itemID, rating, review));
    }

    /**
     * Method to submit a rating only to the server
     * @param itemID The itemID being rated
     * @param rating The rating (out of 5)
     */
    public void submitRating(int itemID, int rating) {
        // Submit a rating and review, but with a blank review.
        submitReview(itemID,rating,"");
    }

    /**
     * Method to return all reviews in an ArrayList for a given itemID number
     * @param itemID The item's unique ID number
     * @return ArrayList<Review> The reviews for the itemID
     */
    public ArrayList<Review> getReviews(int itemID) {
        return reviews.get(itemID) ;
    }

    /**
     * Method to calculate and return the mean rating for an item, rounded to the nearest 0.5
     * @param itemID The unique ID of the item in question
     * @return The double value of the mean rating
     */
    public double getMeanRating(int itemID) {
        double total = 0.0 ;
        double count = 0.0 ;
        for (Review review : reviews.get(itemID)){
            // Add the review's rating to the total
            total += review.getRating() ;
            // Increment the count
            count++ ;
        }
        // Divide the total by the count
        double mean =  total / count ;
        // Round to the nearest 0.5 (multiply by 2, round to nearest int, divide by 2)
        mean = Math.floor(mean * 2.0) / 2.0 ;
        return mean ;
    }
    /**
     * Method to calculate and return the mean rating for a chef, based on all their items,
     * rounded to the nearest 0.5
     * @param chefID The unique ID of the Chef in question
     * @return The double value of the mean rating
     */
    public double getMeanChefRating(int chefID) {
        double total = 0.0 ;
        double count = 0.0 ;
        for (int itemID = chefID * 100 ; itemID < (chefID + 1) * 100 ; itemID++) {
            total += getMeanRating(itemID) ;
            count++ ;
        }
        // Divide the total by the count
        double mean =  total / count ;
        // Round to the nearest 0.5 (multiply by 2, round to nearest int, divide by 2)
        mean = Math.floor(mean * 2.0) / 2.0 ;
        return mean ;
    }
    /**
     * Class to store ratings and reviews of items
     */
    public class Review {

        private int itemID ;
        private int rating ;
        private String review ;

        /**
         * Constructor
         * @param itemID The unique ID of the item to which the rating/review belongs
         * @param rating The rating (out of 5)
         * @param review The review
         */
        public Review(int itemID, int rating, String review) {
            this.itemID = itemID ;
            this.rating = rating ;
            this.review = review ;
        }

        public int getRating() {
            return rating ;
        }

        public String getReview() {
            return review ;
        }
    }
}