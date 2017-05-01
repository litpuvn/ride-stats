package edu.ttu.spm.cheapride.model;


import org.json.JSONException;
import org.json.JSONObject;

public class RideEstimateDTO {

    private RideEstimate uber;
    private RideEstimate lyft;

    public RideEstimateDTO(RideEstimate uber, RideEstimate lyft) {
        this.uber = uber;
        this.lyft = lyft;
    }

    public RideEstimate getUber() {
        return uber;
    }

    public RideEstimate getLyft() {
        return lyft;
    }

    public long getTotalArrivalTime() {
        return uber.getPickupEstimate() + lyft.getPickupEstimate();
    }

    public long getLyftArrivalTime() {
        return lyft.getPickupEstimate();
    }

    public long getUberArrivalTime() {
        return uber.getPickupEstimate();
    }

    public double getTotalCost() {
        return uber.getCost() + lyft.getCost();
    }

    public double getUberCost() {
        return uber.getCost();
    }

    public double getLyftCost() {
        return lyft.getCost();
    }


    public static RideEstimateDTO createFromJson(JSONObject ride) {
        RideEstimate uber = null;
        RideEstimate lyft = null;

        if (ride == null) {
            uber = RideEstimate.createRandomRideEstimate();
            lyft = RideEstimate.createRandomRideEstimate();

            return new RideEstimateDTO(uber, lyft);
        }

        try {
            JSONObject uberJson = ride.getJSONObject("uber");
            uber = uberJson != null ? RideEstimate.createFromJson(uberJson) :  RideEstimate.createEmptyRideEstimate();
         }
        catch (JSONException je) {
            uber = RideEstimate.createEmptyRideEstimate();
        }

        try {
            JSONObject lyftJson = ride.getJSONObject("lyft");
            lyft = lyftJson != null ? RideEstimate.createFromJson(lyftJson) :  RideEstimate.createEmptyRideEstimate();
        }
        catch (JSONException je) {
            lyft =  RideEstimate.createEmptyRideEstimate();
        }

        return new RideEstimateDTO(uber, lyft);
    }


}
