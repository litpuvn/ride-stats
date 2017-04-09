package edu.ttu.spm.cheapride.model;


import org.json.JSONObject;

public class RideEstimate {
    private long time; // seconds
    private double cost; //$
    private long pickupEstimate; // seconds
    private String rideRequestId;

    private RideEstimate() {

    }

    public RideEstimate(long time, double cost, long pickupEstimate, String rideRequestId) {
        this.time = time;
        this.cost = cost;
        this.pickupEstimate = pickupEstimate;
        this.rideRequestId = rideRequestId;
    }



    public long getTime() {
        return time;
    }

    public double getCost() {
        return cost;
    }

    public long getPickupEstimate() {
        return pickupEstimate;
    }

    public String getRideRequestId() {
        return rideRequestId;
    }

    public static RideEstimate createFromJson(JSONObject json) {
        RideEstimate rideEst = new RideEstimate();

        return rideEst;
    }
}
