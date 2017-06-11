package edu.ttu.spm.cheapride.model.item;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/27.
 */

public class rideCompany {
    private String companyName;
    private int cost;
    private int pickupTime;

    public rideCompany(String companyName) {
        this.companyName = companyName;
    }

    public rideCompany(String companyName, int cost, int pickupTime) {
        this.companyName = companyName;
        this.cost = cost;
        this.pickupTime = pickupTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCost() {
        return cost;
    }

    public int getPickupTime() {
        return pickupTime;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setPickupTime(int pickupTime) {
        this.pickupTime = pickupTime;
    }

    public static rideCompany createMe(String rideCompany){
        rideCompany company = new rideCompany(rideCompany);
        company.setCost((int) (Math.random() * 80));
        company.setPickupTime((int) (Math.random() * 20));

        return company;
    }

    public JSONObject toJson(){

            JSONObject com = new JSONObject();
        try {
            com.put("companyName", this.companyName);
            com.put("cost", this.cost);
            com.put("pickupTime", this.pickupTime);
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return com;
    }


    public static rideCompany createFromJSONObject(JSONObject jsonObject){
        rideCompany rideCompany = null;
        try{
            String companyName = jsonObject.getString("companyName");
            int cost = jsonObject.getInt("cost");
            int pickupTime = jsonObject.getInt("pickupTime");

            rideCompany = new rideCompany(companyName,cost,pickupTime);
        }
        catch (JSONException je) {
            je.printStackTrace();
        }

        return rideCompany;
    }
}
