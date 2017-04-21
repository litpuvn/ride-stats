package edu.ttu.spm.cheapride;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.ttu.spm.cheapride.handler.EstimateHandler;
import edu.ttu.spm.cheapride.listener.MyPlaceSelectionListener;
import edu.ttu.spm.cheapride.model.RideEstimate;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

//    public static final String BASE_URL = "http://cheapride-api.dtag.vn:8080/cheapRide";
        public static final String BASE_URL = "http://192.168.0.110:8080/cheapRide";
    private static final String TAG = MainActivity.class.getSimpleName();
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // A default location Texas Tech University and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(33.5842591, -101.8782822);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LatLng mCurrentLocation;

    private PlaceAutocompleteFragment autocompleteFragment;

    private static final int LOGIN_REQUEST = 0;

    private TextView loginTextView;
    private TextView registerTextView;
    private TextView loginSeparatorTextView;
    private TextView welcomeTextView;

    private EstimateHandler estimateManager;

    private View comparisonChart;
    private TextView uberArrivalTime;
    private TextView lyftArrivalTime;
    private TextView uberCost;
    private TextView lyftCost;

    private static final int CHART_MAX_WIDTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_main);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        loginTextView = (TextView) findViewById(R.id.login);
        registerTextView = (TextView) findViewById(R.id.register);
        loginSeparatorTextView = (TextView) findViewById(R.id.login_separator);
        welcomeTextView = (TextView) findViewById(R.id.welcome_message);
        comparisonChart = findViewById(R.id.comparison_chart);

        uberArrivalTime = (TextView) findViewById(R.id.uber_arrival);
        lyftArrivalTime = (TextView) findViewById(R.id.lyft_arrival);
        uberCost = (TextView) findViewById(R.id.uber_cost);
        lyftCost = (TextView) findViewById(R.id.lyft_cost);


        estimateManager = new EstimateHandler(this);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        autocompleteFragment.setOnPlaceSelectedListener(new MyPlaceSelectionListener(this, this.estimateManager, mMap, mCurrentLocation, DEFAULT_ZOOM));

    }

    public void activateComparisonChart(RideEstimateDTO rideEstimateDto) {

        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float pxRatio = ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        double uberTimeWidth = rideEstimateDto.getTotalArrivalTime() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getUberArrivalTime() / rideEstimateDto.getTotalArrivalTime())) : 0;
        double lyftTimeWidth = rideEstimateDto.getTotalArrivalTime() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getLyftArrivalTime() / rideEstimateDto.getTotalArrivalTime())) : 0;

        this.uberArrivalTime.setWidth((int)(uberTimeWidth * pxRatio));
        this.uberArrivalTime.setText(String.valueOf(rideEstimateDto.getUberArrivalTime() / 60));
        this.lyftArrivalTime.setWidth((int)(lyftTimeWidth * pxRatio));
        this.lyftArrivalTime.setText(String.valueOf(rideEstimateDto.getLyftArrivalTime() / 60));

        double uberCostWidth = rideEstimateDto.getTotalCost() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getUberCost() / rideEstimateDto.getTotalCost())) : 0;
        double lyftCostWidth = rideEstimateDto.getTotalCost() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getLyftCost() / rideEstimateDto.getTotalCost())) : 0;
        this.uberCost.setWidth((int) (uberCostWidth * pxRatio));
        this.uberCost.setText(String.valueOf(rideEstimateDto.getUberCost()));
        this.lyftCost.setWidth((int) (lyftCostWidth * pxRatio));
        this.lyftCost.setText(String.valueOf(rideEstimateDto.getLyftCost()));

        this.comparisonChart.setVisibility(View.VISIBLE);
    }


    public void onLoginClicked(View v) {
        System.out.println("Login clicked");

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent loginResponse) {
        // Check which request we're responding to
        if (requestCode == LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String response = loginResponse.getStringExtra("response");
                System.out.println("My response: " + response);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());

                loginTextView.setVisibility(View.INVISIBLE);
                registerTextView.setVisibility(View.INVISIBLE);
                loginSeparatorTextView.setVisibility(View.INVISIBLE);
                welcomeTextView.setText("Hello, Today is " + formattedDate);
                welcomeTextView.setVisibility(View.VISIBLE);
            }
            else {
                welcomeTextView.setText("");
                welcomeTextView.setVisibility(View.INVISIBLE);

            }
        }
    }

    public void onRegisterClicked(View v) {
        System.out.println("register clicked");

        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            //Not in api-23, no need to prompt
            mMap.setMyLocationEnabled(true);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            mCurrentLocation =  new LatLng(lat, lng);
            //mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("Your Location"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));
        }
        else
        {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }

    private void moveCamemra() {

    }

}


