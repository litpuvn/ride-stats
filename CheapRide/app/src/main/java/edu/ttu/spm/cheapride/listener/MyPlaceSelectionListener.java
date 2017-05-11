package edu.ttu.spm.cheapride.listener;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.handler.EstimateHandler;
import edu.ttu.spm.cheapride.model.BookRequest;


public class MyPlaceSelectionListener implements PlaceSelectionListener {

    private static final String TAG = MyPlaceSelectionListener.class.getSimpleName();
    private GoogleMap mMap;
    private int zoomLevel;
    private LatLng currentLocation;
    private EstimateHandler estimateManager;
    private Context mContext;
    private Marker destinationMarker;

    private ArrayList<Polyline> routes = new ArrayList<>();

    public MyPlaceSelectionListener(Context mContext, EstimateHandler estimateManager, GoogleMap mMap, LatLng currentLocation, int zoomLevel) {
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
        Log.i(TAG, "Address: " + place.getAddress());
        Log.i(TAG, "Place LatLng: " + place.getLatLng().toString());

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        for (Iterator<Polyline> i = routes.iterator(); i.hasNext();) {
            Polyline item = i.next();
            item.remove();
        }

        mMap.clear();
        MainActivity m = (MainActivity)mContext;
        m.displayCurrentLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), this.zoomLevel));
        destinationMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));


        String serverKey = "AIzaSyCOlafJC7QHMEiBqCfd0cDmdbLU1ZwkdHA";
        LatLng origin = this.currentLocation;
        LatLng destination = place.getLatLng();

        this.estimateManager.attemptEstimate(origin, destination, m.getSelectedCarTypeAsString());

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
                            Polyline tmpRoute = mMap.addPolyline(polylineOptions);

                            routes.add(tmpRoute);
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


