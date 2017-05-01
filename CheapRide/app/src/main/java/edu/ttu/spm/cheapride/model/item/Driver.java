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

        if (json == null) {
            return createRandomDriver();
        }
        try {
            String phone = json.getString("phone_number");
            float rating = (float)json.getDouble("rating");
            String firstName = json.getString("firstName");
//            String imageUrl = json.getString("imageUrl");
            String imageUrl = "https://public-api.lyft.com/static/images/user.png";

            dr = new Driver(phone, rating, firstName, imageUrl);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return dr;
    }

    private static Driver createRandomDriver() {
        Driver d;
        String phone = String.valueOf((long)900000000 + (long)(999999999*Math.random()));
        float rating = (float)(5* Math.random());
        String firstName = "John";
        String imageUrl = "https://public-api.lyft.com/static/images/user.png";
        return  new Driver(phone, rating, firstName, imageUrl);
    }
}


