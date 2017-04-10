package edu.ttu.spm.cheapride.model;


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
        RideEstimate uber = RideEstimate.createFromJson(null);
        RideEstimate lyft = RideEstimate.createFromJson(null);

        RideEstimateDTO dto = new RideEstimateDTO(uber, lyft);
        return dto;
    }


}
