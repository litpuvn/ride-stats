package edu.ttu.spm.cheapride.model;

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


}
