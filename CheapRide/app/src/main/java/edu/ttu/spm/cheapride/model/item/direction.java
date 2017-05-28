package edu.ttu.spm.cheapride.model.item;

/**
 * Created by Administrator on 2017/5/27.
 */

public class direction {

    private String direction;
    private rideCompany uber;
    private rideCompany lyft;

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
}
