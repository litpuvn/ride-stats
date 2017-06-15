package edu.ttu.spm.cheapride.model.item;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Administrator on 2017/5/30.
 */

public class Asset implements ClusterItem {
    public  String locationName;
    private LatLng mPosition;

    private double uber_east;
    private double uber_west;
    private double uber_south;
    private double uber_north;

    private double lyft_east;
    private double lyft_west;
    private double lyft_north;
    private double lyft_south;

    public static final int MIN = 0;
    public static final int MAX = 10;
    public static final int VALUE = MAX-MIN;

    public static final int MIN_SCALE = 20;
    public static final int MAX_SCALE = 100;
    public static final int RANGE = MAX_SCALE-MIN_SCALE;

    public Asset(String locationName) {
        this.locationName = locationName;
    }
    public Asset(String locationName,LatLng mPosition){
        this.locationName = locationName;
        this.mPosition = mPosition;

        this.uber_east = MIN + Math.random() * VALUE;
        this.uber_west = MIN + Math.random() * VALUE;
        this.uber_north = MIN + Math.random() * VALUE;
        this.uber_south = MIN + Math.random() * VALUE;

        this.lyft_east = MIN + Math.random() * VALUE;
        this.lyft_west = MIN + Math.random() * VALUE;
        this.lyft_north = MIN + Math.random() * VALUE;
        this.lyft_south = MIN + Math.random() * VALUE;
    }
     public Asset(String locationName, double lat,double lng) {
        this.locationName = locationName;
        mPosition = new LatLng(lat,lng);

         this.uber_east = MIN + Math.random() * VALUE;
         this.uber_west = MIN + Math.random() * VALUE;
         this.uber_north = MIN + Math.random() * VALUE;
         this.uber_south = MIN + Math.random() * VALUE;

         this.lyft_east = MIN + Math.random() * VALUE;
         this.lyft_west = MIN + Math.random() * VALUE;
         this.lyft_north = MIN + Math.random() * VALUE;
         this.lyft_south = MIN + Math.random() * VALUE;
    }

    public void setmPosition(LatLng pos) {
        mPosition = pos;
    }

    public String getLocationName() {
        return locationName;
    }

    @Override


    public LatLng getPosition() {
        return mPosition;
    }

    public double getLat(){
        return mPosition.latitude;
    }

    public double getLng(){
        return mPosition.longitude;
    }

    public double getUber_east() {
        return uber_east;
    }

    public double getUber_west() {
        return uber_west;
    }

    public double getUber_south() {
        return uber_south;
    }

    public double getUber_north() {
        return uber_north;
    }

    public double getLyft_east() {
        return lyft_east;
    }

    public double getLyft_west() {
        return lyft_west;
    }

    public double getLyft_north() {
        return lyft_north;
    }

    public double getLyft_south() {
        return lyft_south;
    }

    public void setUber_east(double uber_east) {
        this.uber_east = uber_east;
    }

    public void setUber_west(double uber_west) {
        this.uber_west = uber_west;
    }

    public void setUber_south(double uber_south) {
        this.uber_south = uber_south;
    }

    public void setUber_north(double uber_north) {
        this.uber_north = uber_north;
    }

    public void setLyft_east(double lyft_east) {
        this.lyft_east = lyft_east;
    }

    public void setLyft_west(double lyft_west) {
        this.lyft_west = lyft_west;
    }

    public void setLyft_north(double lyft_north) {
        this.lyft_north = lyft_north;
    }

    public void setLyft_south(double lyft_south) {
        this.lyft_south = lyft_south;
    }
}
