package edu.ttu.spm.cheapride.model.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/27.
 */

public class direction {

    private String direction;
    private rideCompany uber;
    private rideCompany lyft;

    direction(String direction) {
        this.direction = direction;
    }

    public direction(String direction, rideCompany uber, rideCompany lyft) {
        this.direction = direction;
        this.uber = uber;
        this.lyft = lyft;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public rideCompany getUber() {
        return uber;
    }

    public void setUber(rideCompany uber) {
        this.uber = uber;
    }

    public rideCompany getLyft() {
        return lyft;
    }

    public void setLyft(rideCompany lyft) {
        this.lyft = lyft;
    }


    public static direction createMe(String direction) {
        direction dir = new direction(direction);
        dir.setUber(rideCompany.createMe("Uber"));
        dir.setLyft(rideCompany.createMe("Lyft"));

        return dir;
    }

    public JSONObject toJson(){
        JSONObject direction = new JSONObject();
        try {
            direction.put("direction", this.direction);
            direction.put("Uber", this.uber.toJson());
            direction.put("Lyft", this.lyft.toJson());
        }
        catch (JSONException je){
            je.printStackTrace();
        }
        return direction;
    }

    public static direction createFromJSONObject(JSONObject jsonObject){
        direction direction = null;
        try{
            String directionName = jsonObject.getString("direction");

            JSONObject UberJson = (JSONObject)jsonObject.get("Uber");
            rideCompany Uber= rideCompany.createFromJSONObject(UberJson);

            JSONObject LyftJson = (JSONObject)jsonObject.get("Lyft");
            rideCompany Lyft = rideCompany.createFromJSONObject(LyftJson);

            direction = new direction(directionName,Uber,Lyft);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }
        return direction;
    }
}
