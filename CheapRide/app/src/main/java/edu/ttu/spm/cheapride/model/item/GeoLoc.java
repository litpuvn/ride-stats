package edu.ttu.spm.cheapride.model.item;


import org.json.JSONException;
import org.json.JSONObject;

public class GeoLoc {

    private double lat;
    private double lng;

    public GeoLoc(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject geo = new JSONObject();
        geo.put("lat", this.lat);
        geo.put("lng", this.lng);

        return geo;
    }
}
