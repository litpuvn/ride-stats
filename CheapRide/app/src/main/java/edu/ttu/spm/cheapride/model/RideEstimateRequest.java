package edu.ttu.spm.cheapride.model;

import com.google.android.gms.maps.model.LatLng;

public class RideEstimateRequest {

    private LatLng origin;
    private LatLng destination;
    private String carType;

    public RideEstimateRequest(LatLng origin, LatLng destination, String carType) {
        this.origin = origin;
        this.destination = destination;
        this.carType = carType;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public String getCarType() {
        return carType;
    }
}
