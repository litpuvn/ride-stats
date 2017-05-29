package edu.ttu.spm.cheapride.model.item;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Administrator on 2017/5/28.
 */

public class clusterItem implements ClusterItem {

    private final LatLng mPosition;

    public clusterItem(double latitude, double longitude) {
        mPosition = new LatLng(latitude, longitude);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
