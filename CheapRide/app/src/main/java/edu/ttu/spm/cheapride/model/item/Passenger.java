package edu.ttu.spm.cheapride.model.item;

import org.json.JSONObject;

import java.security.Permission;

public class Passenger {
    private double rating;
    private String firstName;
    private String lastName;
    private String userId;
    private String imageUrl;

    public Passenger(double rating, String firstName, String lastName, String userId, String imageUrl) {
        this.rating = rating;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static Passenger createFromJson(JSONObject json) {
        Passenger passenger = null;


        return passenger;
    }
}


