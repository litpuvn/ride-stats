package edu.ttu.spm.cheapride.model;


import org.json.JSONException;
import org.json.JSONObject;

import edu.ttu.spm.cheapride.model.item.GeoLoc;

public class BookRequest {
    private String provider;
    private String rideType;
    private GeoLoc origin;
    private GeoLoc destination;

    public BookRequest(String provider, String rideType, GeoLoc origin, GeoLoc destination) {
        this.provider = provider;
        this.rideType = rideType;
        this.origin = origin;
        this.destination = destination;
    }

    public String getProvider() {
        return provider;
    }

    public String getRideType() {
        return rideType;
    }

    public GeoLoc getOrigin() {
        return origin;
    }

    public GeoLoc getDestination() {
        return destination;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject req = new JSONObject();
        req.put("provider", this.provider);
        req.put("ride_type", this.rideType);
        req.put("origin", this.origin.toJson());
        req.put("destination", this.destination.toJson());
        return req;
    }
}
