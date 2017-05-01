package edu.ttu.spm.cheapride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import edu.ttu.spm.cheapride.handler.BookingHandler;
import edu.ttu.spm.cheapride.handler.EstimateHandler;
import edu.ttu.spm.cheapride.listener.MyPlaceSelectionListener;
import edu.ttu.spm.cheapride.model.BookResponse;
import edu.ttu.spm.cheapride.model.RideEstimate;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;
import edu.ttu.spm.cheapride.model.item.Driver;
import edu.ttu.spm.cheapride.model.item.Vehicle;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        LocationListener,
        AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int LOCATION_REQUEST = 1340;

    //public static final String BASE_URL = "http://cheapride-api.dtag.vn:8080/cheapRide";
    public static final String BASE_URL = "http://738e44ce.ngrok.io/cheapRide";
//        public static final String BASE_URL = "http://192.168.0.110:8080/cheapRide";
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
    private static final String[] CAR_TYPES = {"Any", "Share", "4 seats", "6 or more seats", "Luxury 4 seats"};
    private static final Map<Integer, String> CAR_TYPE_MAP;

    static {
        Hashtable<Integer, String> tmp = new Hashtable<>();
        tmp.put(0, "");
        tmp.put(1, "share");
        tmp.put(2, "4_seats");
        tmp.put(3, "6_or_more_seats");
        tmp.put(4, "luxury_4_seats");

        CAR_TYPE_MAP = Collections.unmodifiableMap(tmp);
    }

    private TextView loginTextView;
    private TextView registerTextView;
    private TextView loginSeparatorTextView;
    private TextView welcomeTextView;

    private EstimateHandler estimateManager;
    private BookingHandler bookingHandler;

    private View comparisonChart;
    private View rideBooking;
    private View bookingButtons;
    private View driveInfoBoard;

    private ImageView vehicleImage;
    private TextView vehicleColor;
    private TextView vehiclePlateLicense;
    private TextView vehicleInfo;
    private ImageView driverImage;
    private TextView driverName;
    private TextView driverInfo;

    private TextView uberArrivalTime;
    private TextView lyftArrivalTime;
    private TextView uberCost;
    private TextView lyftCost;

    private Spinner carTypeSelection;

    private static final int CHART_MAX_WIDTH = 100;
    private TrackGPS gps;

    private int selectedCarType = 0;

    private ImageLoader imageLoader;

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
        rideBooking = findViewById(R.id.ride_booking);
        driveInfoBoard = findViewById(R.id.driverInfoBoard);
        bookingButtons = findViewById(R.id.bookingButtons);

        vehicleImage = (ImageView) findViewById(R.id.vehicleImg);
        vehicleColor = (TextView) findViewById(R.id.vehicleColor);
        vehiclePlateLicense = (TextView) findViewById(R.id.vehiclePlateLicense);
        vehicleInfo = (TextView) findViewById(R.id.vehicleInfo);
        driverImage = (ImageView) findViewById(R.id.driverImg);
        driverName = (TextView) findViewById(R.id.driverName);
        driverInfo = (TextView) findViewById(R.id.driverInfo);

        uberArrivalTime = (TextView) findViewById(R.id.uber_arrival);
        lyftArrivalTime = (TextView) findViewById(R.id.lyft_arrival);
        uberCost = (TextView) findViewById(R.id.uber_cost);
        lyftCost = (TextView) findViewById(R.id.lyft_cost);


        carTypeSelection = (Spinner)findViewById(R.id.carType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,CAR_TYPES);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carTypeSelection.setAdapter(adapter);
        carTypeSelection.setOnItemSelectedListener(this);

        bookingHandler = new BookingHandler(this);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
            .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
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
//        getDeviceLocation();

        estimateManager = new EstimateHandler(this);

        if (!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
        else {
            gps = new TrackGPS(this);
            this.displayLocation(gps.getLatitude(), gps.getLongitude());
        }

        autocompleteFragment.setOnPlaceSelectedListener(new MyPlaceSelectionListener(this, this.estimateManager, mMap, mCurrentLocation, DEFAULT_ZOOM));

    }

    public void activateComparisonChart(RideEstimateDTO rideEstimateDto) {

        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float pxRatio = ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        double uberTimeWidth = this.getUberTimeWidth(rideEstimateDto);
        double lyftTimeWidth = this.getLyftTimeWidth(rideEstimateDto);

        this.uberArrivalTime.setWidth((int)(uberTimeWidth * pxRatio));
        this.uberArrivalTime.setText(String.valueOf(rideEstimateDto.getUberArrivalTime() / 60));
        this.lyftArrivalTime.setWidth((int)(lyftTimeWidth * pxRatio));
        this.lyftArrivalTime.setText(String.valueOf(rideEstimateDto.getLyftArrivalTime() / 60));

        double uberCostWidth = this.getUberCostWidth(rideEstimateDto);
        double lyftCostWidth = this.getLyftCostWidth(rideEstimateDto);

        this.uberCost.setWidth((int) (uberCostWidth * pxRatio));
        this.uberCost.setText(String.valueOf(rideEstimateDto.getUberCost()));
        this.lyftCost.setWidth((int) (lyftCostWidth * pxRatio));
        this.lyftCost.setText(String.valueOf(rideEstimateDto.getLyftCost()));

        this.comparisonChart.setVisibility(View.VISIBLE);

        if (LoginActivity.isLogin) {
            this.rideBooking.setVisibility(View.VISIBLE);
            bookingButtons.setVisibility(View.VISIBLE);
        }
    }

    public double getUberTimeWidth(RideEstimateDTO rideEstimateDto) {
        return rideEstimateDto.getTotalArrivalTime() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getUberArrivalTime() / rideEstimateDto.getTotalArrivalTime())) : 0;
    }

    public double getUberCostWidth(RideEstimateDTO rideEstimateDto) {
        return rideEstimateDto.getTotalCost() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getUberCost() / rideEstimateDto.getTotalCost())) : 0;
    }

    public double getLyftCostWidth(RideEstimateDTO rideEstimateDto) {
        return rideEstimateDto.getTotalCost() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getLyftCost() / rideEstimateDto.getTotalCost())) : 0;
    }

    public double getLyftTimeWidth(RideEstimateDTO rideEstimateDto) {
        return rideEstimateDto.getTotalArrivalTime() != 0 ? (CHART_MAX_WIDTH * (1.0 * rideEstimateDto.getLyftArrivalTime() / rideEstimateDto.getTotalArrivalTime())) : 0;
    }




    public void onLoginClicked(View v) {
        System.out.println("Login clicked");

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST);

    }

    public void onUberClick(View v) {
        RideEstimate uber = this.estimateManager.getRideEstimateResponse().getUber();
        if (uber == null || uber.getRideRequestId() == null || uber.getRideRequestId().length() < 1) {
            Log.i(TAG, "Invalid  uber booking  No estimate returned from estimate request");
            return;
        }

        if (this.bookingHandler.isBooking()) {
            Log.i(TAG, "Another booking is processing");
            return;
        }

        this.bookingHandler.doBooking(uber.getRideRequestId());
    }

    public void onLyftClick(View v) {
        RideEstimate lyft = this.estimateManager.getRideEstimateResponse().getLyft();
        if (lyft == null || lyft.getRideRequestId() == null || lyft.getRideRequestId().length() < 1) {
            Log.i(TAG, "Invalid lyft booking.  No estimate returned from estimate request");
            return;
        }

        if (this.bookingHandler.isBooking()) {
            Log.i(TAG, "Another booking is processing");
            return;
        }

        this.bookingHandler.doBooking(lyft.getRideRequestId());
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

                LoginActivity.isLogin = true;
            }
            else {
                welcomeTextView.setText("");
                welcomeTextView.setVisibility(View.INVISIBLE);
                LoginActivity.isLogin = false;

            }
        }
    }

    public void showBookResponse(BookResponse bookResponse) {
        if (bookResponse == null || !bookResponse.isAccepted()) {
            return;
        }

        Vehicle v = bookResponse.getVehicle();
        Driver d = bookResponse.getDriver();
        bookingButtons.setVisibility(View.INVISIBLE);

        // show info
        imageLoader.displayImage(v.getImageUrl(), vehicleImage);
        vehiclePlateLicense.setText(v.getLicense());
        vehicleColor.setText(v.getColor());
        vehicleInfo.setText(v.getBasicPrudctionInfo());

        imageLoader.displayImage(d.getImageUrl(), driverImage);
        driverName.setText(d.getFirstName());
        driverInfo.setText(d.getPhoneNumber());

        driveInfoBoard.setVisibility(View.VISIBLE);
    }

    public void onRegisterClicked(View v) {
        System.out.println("register clicked");

        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
    }

    public LatLng getCurrentLocation() {
        return this.mCurrentLocation;
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

    public void displayCurrentLocation() {
        mCurrentLocation =  new LatLng(gps.getLatitude(), gps.getLongitude());

        this.displayLocation(gps.getLatitude(), gps.getLongitude());
    }

    private void displayLocation(double lat, double lng) {
        mCurrentLocation =  new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
        mMap.animateCamera(zoom);
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
            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
            mMap.animateCamera(zoom);

        }
        else
        {
            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "location permission result");
        switch(requestCode) {

            case LOCATION_REQUEST:
                gps = new TrackGPS(this);
                this.displayLocation(gps.getLatitude(), gps.getLongitude());
                Log.d(TAG, "longtitude: " + gps.getLongitude() + "; latitude: " + gps.getLatitude());

                break;
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(this, perm));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");

        gps.setLocation(location);
        this.displayLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

       this.selectedCarType = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getSelectedCarTypeAsString() {
        if (this.selectedCarType <= 0 || this.selectedCarType >= CAR_TYPES.length) {
            this.selectedCarType = 0;
        }

        return CAR_TYPE_MAP.get(this.selectedCarType);
    }
}


