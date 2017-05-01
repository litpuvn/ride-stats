package edu.ttu.spm.cheapride.model.item;


import org.json.JSONException;
import org.json.JSONObject;

public class Address {
    private double lat;
    private double lon;
    private int eta; // estimate time of arrival in seconds
    private String address;


    public Address(double lat, double lon, int eta, String address) {
        this.lat = lat;
        this.lon = lon;
        this.eta = eta;
        this.address = address;
    }


    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getEta() {
        return eta;
    }

    public String getAddress() {
        return address;
    }

    public static Address createFromJson(JSONObject json) {
        Address addr = null;
        try {

            double la = json.getDouble("lat");
            double lo = json.getDouble("lng");
            int et = json.getInt("eta_seconds");
            String ad = json.getString("address");

            addr = new Address(la, lo, et, ad);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return addr;
    }
}


