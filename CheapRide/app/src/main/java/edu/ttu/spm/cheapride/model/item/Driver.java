package edu.ttu.spm.cheapride.model.item;


import org.json.JSONException;
import org.json.JSONObject;

public class Driver {
    private String phoneNumber;
    private float rating;
    private String firstName;
    private String imageUrl;

    public Driver(String phoneNumber, float rating, String firstName, String imageUrl) {
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.firstName = firstName;
        this.imageUrl = imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static Driver createFromJson(JSONObject json) {
        Driver dr = null;

        try {
            String phone = json.getString("phone");

            dr = new Driver(phone);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return dr;
    }
}


