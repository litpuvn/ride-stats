package edu.ttu.spm.cheapride.model;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ttu.spm.cheapride.model.item.Address;
import edu.ttu.spm.cheapride.model.item.Driver;
import edu.ttu.spm.cheapride.model.item.Passenger;
import edu.ttu.spm.cheapride.model.item.Vehicle;

public class BookResponse {
    private String status;
    private String rideRequestId;
    private Address origin;
    private Passenger passenger;
    private Address destination;
    private Driver driver;

    private String requestedTime;
    private Vehicle vehicle;

    public BookResponse(String status, Address origin, Passenger passenger, Address destination, Driver driver, String requestedTime, Vehicle vehicle) {
        this.status = status;
        this.origin = origin;
        this.passenger = passenger;
        this.destination = destination;
        this.driver = driver;
        this.requestedTime = requestedTime;
        this.vehicle = vehicle;
    }

    public BookResponse(String status, String rideRequestId, Driver driver, Vehicle vehicle) {
        this.status = status;
        this.rideRequestId = rideRequestId;
        this.driver = driver;
        this.vehicle = vehicle;
    }

    public String getStatus() {
        return status;
    }

    public String getRideRequestId() {
        return rideRequestId;
    }

    public Driver getDriver() {
        return driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isAccepted() {
        if (this.status == null) {
            return false;
        }

        return this.status.toLowerCase().equals("accepted") ;
    }

    public static BookResponse createFromJson(JSONObject bookResponse) {

        BookResponse response = null;

        if (bookResponse == null) {
            return new BookResponse("accepted", "testRideRequest", Driver.createFromJson(null), Vehicle.createFromJson(null));
        }

        try {
            String bStatus = bookResponse.getString("status");
            String rideRequestId = bookResponse.getString("rideId");
//            Address origin = Address.createFromJson(bookResponse.getJSONObject("origin"));
//            Passenger p = Passenger.createFromJson(bookResponse.getJSONObject("passenger"));
//            Address destination = Address.createFromJson(bookResponse.getJSONObject("destination"));
            Driver dr = Driver.createFromJson(bookResponse.getJSONObject("driver"));
//            String requestedTime = bookResponse.getString("requestedTime");
            Vehicle v = Vehicle.createFromJson(bookResponse.getJSONObject("car"));
//            response = new BookResponse(bStatus, origin, p, destination, dr, requestedTime, v);
            response = new BookResponse(bStatus, rideRequestId, dr,  v);

        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return  response;

    }
}
