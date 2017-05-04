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

    public String getLicense() {
        return this.licensePlateState + "-" + this.licensePlate;
    }

    public String getBasicPrudctionInfo() {
        return this.make + "-" + this.model;
    }

    public static Vehicle createFromJson(JSONObject json) {
        Vehicle v = null;
        if (json == null) {
            return createRandomVehicle();
        }
        try {
//            String color = json.getString("color");
            String color = "Green";

            String make = json.getString("make");
            String licensePlate = json.getString("license_plate");
            String year = json.getString("year");
            String licensePlateState = json.getString("license_plate_state");
            String model = json.getString("model");
            String imageUrl = json.getString("imageUrl");
//            String imageUrl = "https://public-api.lyft.com/static/images/prius_blue.png";

            v = new Vehicle(color, make, licensePlate, year, licensePlateState, model, imageUrl);
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return v;
    }

    private static Vehicle createRandomVehicle() {
        String color = "Green";
        String make = "Toyota";
        String licensePlate = String.valueOf(1000 + (int)(10000*Math.random()));
        String licenseState = "Txt" ;
        String year = String.valueOf(2000 + (int)(20 * Math.random()));

        String imageUrl = "https://public-api.lyft.com/static/images/prius_blue.png";

        return new Vehicle(color, make, licensePlate, year, licenseState, "", imageUrl);
    }


}
