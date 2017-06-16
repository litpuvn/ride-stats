package edu.ttu.spm.cheapride.model.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/27.
 */

public class Origin {

    private double lat;
    private double lng;

    private String name;

    private direction east;
    private  direction west;
    private direction north;
    private direction south;

    public Origin() {
    }

    public Origin(double lat, double lng, String name, direction east, direction west, direction north, direction south) {
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


    public static Origin createMe(String name) {
        Origin a = new Origin();
        a.setName(name);
        a.setLat((Math.random() * ((36.615905 - 30.325877) + 1)) + 30.325877);
        a.setLng((Math.random() * ((60.648976 - 47.465382) + 1)) + 47.465382);
        a.setEast(direction.createMe("east"));
        a.setWest(direction.createMe("west"));
        a.setNorth(direction.createMe("north"));
        a.setSouth(direction.createMe("south"));

        return a;
    }

    public JSONObject toJsonObject() {
        JSONObject myObject = new JSONObject();


        return  null;
    }
    public String toJsonString() {
        JSONObject jsonObj = this.toJsonObject();
        return jsonObj.toString();
    }

    public static Origin createFromJsonObject(JSONObject obj) {

        Origin origin = null;


        try{

            Double lat = obj.getDouble("lat");
            Double lng = obj.getDouble("lon");
            String name = obj.getString("name");

            JSONObject eastJson = (JSONObject)obj.get("east");
            direction east = direction.createFromJSONObject("east", eastJson);

            JSONObject westJson = (JSONObject)obj.get("west");
            direction west = direction.createFromJSONObject("west", westJson);

            JSONObject northJson = (JSONObject)obj.get("north");
            direction north = direction.createFromJSONObject("north", northJson);

            JSONObject southJson = (JSONObject)obj.get("south");
            direction south = direction.createFromJSONObject("south", southJson);

            origin = new Origin(lat,lng,name,east,west,north,south);

        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return origin;
    }

    public JSONObject toJson() {
        JSONObject oir = new JSONObject();
        try {
            oir.put("lat", this.lat);
            oir.put("lng", this.lng);
            oir.put("name", this.name);
            oir.put("east", this.east.toJson());
            oir.put("west", this.west.toJson());
            oir.put("south", this.south.toJson());
            oir.put("north", this.north.toJson());
        }
        catch (JSONException je){
            je.printStackTrace();
        }
        return oir;
    }
}
