package edu.ttu.spm.cheapride.model.item;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle {
    private String color;
    private String make; // vehicle brand
    private String licensePlate;
    private String year;
    private String licensePlateState;
    private String model;
    private String imageUrl;

    public Vehicle(String color, String make, String licensePlate, String year, String licensePlateState, String model, String imageUrl) {
        this.color = color;
        this.make = make;
        this.licensePlate = licensePlate;
        this.year = year;
        this.licensePlateState = licensePlateState;
        this.model = model;
        this.imageUrl = imageUrl;
    }

    public String getColor() {
        return color;
    }

    public String getMake() {
        return make;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getYear() {
        return year;
    }

    public String getLicensePlateState() {
        return licensePlateState;
    }

    public String getModel() {
        return model;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static Vehicle createFromJson(JSONObject json) {
        Vehicle v = null;

        try {
            String color = json.getString("color");

            v = new Vehicle(color);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return v;
    }
}



