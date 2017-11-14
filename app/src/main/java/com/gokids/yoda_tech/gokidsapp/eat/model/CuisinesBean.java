package com.gokids.yoda_tech.gokidsapp.eat.model;

import java.io.Serializable;

/**
 * Created by benepik on 23/6/17.
 */

public class CuisinesBean implements Serializable {
    private String restaurantCuisineID;
    private String RestaurantID;
    private String CuisineID;
    private String Cuisine;

    public String getRestaurantCuisineID() {
        return restaurantCuisineID;
    }

    public void setRestaurantCuisineID(String restaurantCuisineID) {
        this.restaurantCuisineID = restaurantCuisineID;
    }

    public String getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getCuisineID() {
        return CuisineID;
    }

    public void setCuisineID(String cuisineID) {
        CuisineID = cuisineID;
    }

    public String getCuisine() {
        return Cuisine;
    }

    public void setCuisine(String cuisine) {
        Cuisine = cuisine;
    }
}
