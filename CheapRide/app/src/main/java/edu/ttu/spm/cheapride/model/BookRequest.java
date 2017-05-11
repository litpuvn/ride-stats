package edu.ttu.spm.cheapride.model;


import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.item.GeoLoc;

public class BookRequest {
    private String provider;
    private String rideType;
    private GeoLoc origin;
    private GeoLoc destination;

    private Geocoder geocoder;

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

    public String getOriginAsString() {
       return BookRequest.getCompleteAddressString(this.origin.getLat(), this.origin.getLng());
    }

    public String getDestinationAsString() {
        return BookRequest.getCompleteAddressString(this.destination.getLat(), this.destination.getLng());
    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = MainActivity.geocoder;
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strAdd = strReturnedAddress.toString();
                System.out.println("addr: " + strAdd);
            } else {
                System.out.println("No Address returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
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
