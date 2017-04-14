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
        long time = Math.round(600*Math.random());
        double cost = 20*Math.random();
        long pickup = Math.round(600*Math.random());
        String rideRequestId = "Request-" + Math.round(9*Math.random()) + "-" + Math.round(9*Math.random()) + "-" + Math.round(9*Math.random());
        RideEstimate rideEst = new RideEstimate(time, cost, pickup, rideRequestId);

        return rideEst;
    }
}
