package edu.ttu.spm.cheapride.model;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ttu.spm.cheapride.model.item.Address;
import edu.ttu.spm.cheapride.model.item.Driver;
import edu.ttu.spm.cheapride.model.item.Passenger;
import edu.ttu.spm.cheapride.model.item.Vehicle;

public class BookResponse {
    private String status;
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

    public static BookResponse createBookResponseFromJson(JSONObject bookResponse) {

        BookResponse response = null;
        try {
            String bStatus = bookResponse.getString("status");
            Address origin = Address.createFromJson(bookResponse.getJSONObject("origin"));
            Passenger p = Passenger.createFromJson(bookResponse.getJSONObject("passenger"));
            Address destination = Address.createFromJson(bookResponse.getJSONObject("destination"));
            Driver dr = Driver.createFromJson(bookResponse.getJSONObject("driver"));
            String requestedTime = bookResponse.getString("requestedTime");
            Vehicle v = Vehicle.createFromJson(bookResponse.getJSONObject("vehicle"));
            response = new BookResponse(bStatus, origin, p, destination, dr, requestedTime, v);

        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return  response;

    }
}
