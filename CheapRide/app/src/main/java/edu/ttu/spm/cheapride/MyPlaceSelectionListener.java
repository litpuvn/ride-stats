package edu.ttu.spm.cheapride;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MyPlaceSelectionListener implements PlaceSelectionListener {

    private static final String TAG = MyPlaceSelectionListener.class.getSimpleName();
    private GoogleMap mMap;
    private int zoomLevel;
    private LatLng currentLocation;
    private EstimateManager estimateManager;
    private Context mContext;

    public MyPlaceSelectionListener(Context mContext, EstimateManager estimateManager, GoogleMap mMap, LatLng currentLocation, int zoomLevel) {
        this.mContext = mContext;
        this.mMap = mMap;
        this.zoomLevel = zoomLevel;
        this.currentLocation = currentLocation;
        this.estimateManager = estimateManager;
    }

    @Override
    public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName());
        Log.i(TAG, "Place LatLng: " + place.getLatLng().toString());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), this.zoomLevel));
        mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));


        String serverKey = "AIzaSyCOlafJC7QHMEiBqCfd0cDmdbLU1ZwkdHA";
        LatLng origin = this.currentLocation;
        LatLng destination = place.getLatLng();

        this.estimateManager.attemptEstimate(origin, destination);

        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .language(Language.ENGLISH)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        // Do something here
                        System.out.println("Location success: " + rawBody);

                        if(direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(mContext, directionPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
    }


}


