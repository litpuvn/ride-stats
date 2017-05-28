package edu.ttu.spm.cheapride.model.item;

/**
 * Created by Administrator on 2017/5/27.
 */

public class origin {

    private double lat;
    private double lng;

    private String name;

    private direction east;
    private  direction west;
    private direction north;
    private direction south;

    public origin(double lat, double lng, String name, direction east, direction west, direction north, direction south) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.east = east;
        this.west = west;
        this.north = north;
        this.south = south;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public direction getEast() {
        return east;
    }

    public void setEast(direction east) {
        this.east = east;
    }

    public direction getWest() {
        return west;
    }

    public void setWest(direction west) {
        this.west = west;
    }

    public direction getNorth() {
        return north;
    }

    public void setNorth(direction north) {
        this.north = north;
    }

    public direction getSouth() {
        return south;
    }

    public void setSouth(direction south) {
        this.south = south;
    }
}
