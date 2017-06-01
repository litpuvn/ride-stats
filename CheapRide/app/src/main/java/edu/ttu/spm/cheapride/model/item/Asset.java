package edu.ttu.spm.cheapride.model.item;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Administrator on 2017/5/30.
 */

public class Asset implements ClusterItem {
    public final String name;
    //public final int marker;
    private final LatLng mPosition;

//    public Asset(LatLng position, String name, int pictureResource) {
     public Asset(LatLng position, String name) {
        this.name = name;
        /* marker = pictureResource; */
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

//    @Override
//    public String getTitle() {
//        return null;
//    }
//
//    @Override
//    public String getSnippet() {
//        return null;
//    }
}
