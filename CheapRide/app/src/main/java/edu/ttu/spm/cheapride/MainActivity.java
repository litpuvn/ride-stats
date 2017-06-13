package edu.ttu.spm.cheapride;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Geocoder;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import edu.ttu.spm.cheapride.handler.BookingHandler;
import edu.ttu.spm.cheapride.handler.EstimateHandler;
import edu.ttu.spm.cheapride.listener.MyPlaceSelectionListener;
import edu.ttu.spm.cheapride.model.BookResponse;
//import edu.ttu.spm.cheapride.model.ClusteringDemoActivity;
import edu.ttu.spm.cheapride.model.NightingaleRoseChart;
import edu.ttu.spm.cheapride.model.RideEstimate;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;
import edu.ttu.spm.cheapride.model.RideEstimateRequest;
import edu.ttu.spm.cheapride.model.item.Asset;
import edu.ttu.spm.cheapride.model.item.Driver;
import edu.ttu.spm.cheapride.model.item.Origin;
import edu.ttu.spm.cheapride.model.item.Vehicle;
import edu.ttu.spm.cheapride.model.item.clusterItem;
import edu.ttu.spm.cheapride.service.TrackGPS;
import edu.ttu.spm.cheapride.view.DemoView;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.chart.RoseChart;

import static edu.ttu.spm.cheapride.R.id.ad_image_view;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        LocationListener,
        AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ClusterManager.OnClusterClickListener<Asset>, ClusterManager.OnClusterInfoWindowClickListener<Asset>, ClusterManager.OnClusterItemClickListener<Asset>, ClusterManager.OnClusterItemInfoWindowClickListener<Asset> {

    public static final String PROVIDER_UBER = "uber";
    public static final String PROVIDER_LYFT = "lyft";

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int LOCATION_REQUEST = 1340;

    //public static final String BASE_URL = "http://cheapride-api.dtag.vn:8080/cheapRide";
//    public static final String BASE_URL = "http://738e44ce.ngrok.io/cheapRide";
//    public static final String BASE_URL = "http://192.168.1.73:8080/cheapRide";
    public static final String BASE_URL = "http://10.161.19.37:8080/cheapRide";
//    public static final String BASE_URL = "https://a03e04ed.ngrok.io/cheapRide";
//    public static final String BASE_URL = "http://192.168.0.104:8080/cheapRide";
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
    private static final String[] CAR_TYPES = {"4 seats", "Share", "6 or more seats", "Luxury 4 seats"};
    private static final Map<Integer, String> CAR_TYPE_MAP;
    private Context main;

    static {
        Hashtable<Integer, String> tmp = new Hashtable<>();
//        tmp.put(0, "");
        tmp.put(0, "4_seats");
        tmp.put(1, "share");
        tmp.put(2, "6_or_more_seats");
        tmp.put(3, "luxury_4_seats");

        CAR_TYPE_MAP = Collections.unmodifiableMap(tmp);
    }

    private TextView loginTextView;
    private TextView registerTextView;
    private TextView loginSeparatorTextView;
    private TextView welcomeTextView;
    private TextView history;

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

    public EstimateHandler getEstimateManager() {
        return this.estimateManager;
    }

    public static Geocoder geocoder;

    private DemoView mCharts;
    private DemoView mClusterCharts;
    private LinearLayout RoseChart;

    //private ClusterManager<clusterItem> mClusterManager;

    private ClusterManager<Asset> mClusterManager;
    private Random mRandom = new Random(1984);
    private Origin fakeOrigin1;

    double uber_east;
    double uber_west;
    double uber_south;
    double uber_north;

    double lyft_east;
    double lyft_west;
    double lyft_north;
    double lyft_south;

    double cluster_lat;
    double cluster_lng;


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
        history = (TextView) findViewById(R.id.history);
        comparisonChart = findViewById(R.id.comparison_chart);
        rideBooking = findViewById(R.id.ride_booking);
        rideBooking.setVisibility(View.INVISIBLE);
        driveInfoBoard = findViewById(R.id.driverInfoBoard);
        bookingButtons = findViewById(R.id.bookingButtons);
        bookingButtons.setVisibility(View.INVISIBLE);
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


        geocoder = new Geocoder(this, Locale.getDefault());

        main = this;
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

//         //Get the current location of the device and set the position of the map.
//        getDeviceLocation();

//        estimateManager = new EstimateHandler(this);
//
//        if (!canAccessLocation()) {
//            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
//        }
//        else {
//            gps = new TrackGPS(this);
//            this.displayLocation(gps.getLatitude(), gps.getLongitude());
//        }

//        setUpClustering();

        startDemo();

        //autocompleteFragment.setOnPlaceSelectedListener(new MyPlaceSelectionListener(this, this.estimateManager, mMap, mCurrentLocation, DEFAULT_ZOOM));

    }


    public void activateComparisonChart(RideEstimateDTO rideEstimateDto) {

        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float pxRatio = ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        double uberTimeWidth = this.getUberTimeWidth(rideEstimateDto);
        double lyftTimeWidth = this.getLyftTimeWidth(rideEstimateDto);

        this.uberArrivalTime.setWidth((int)(uberTimeWidth * pxRatio));
        this.uberArrivalTime.setText(String.valueOf(rideEstimateDto.getUberArrivalTime()));
        this.lyftArrivalTime.setWidth((int)(lyftTimeWidth * pxRatio));
        this.lyftArrivalTime.setText(String.valueOf(rideEstimateDto.getLyftArrivalTime()));

        double uberCostWidth = this.getUberCostWidth(rideEstimateDto);
        double lyftCostWidth = this.getLyftCostWidth(rideEstimateDto);

        this.uberCost.setWidth((int) (uberCostWidth * pxRatio));
        this.uberCost.setText(String.valueOf(rideEstimateDto.getUberCost()));
        this.lyftCost.setWidth((int) (lyftCostWidth * pxRatio));
        this.lyftCost.setText(String.valueOf(rideEstimateDto.getLyftCost()));

        this.comparisonChart.setVisibility(View.VISIBLE);

        if (LoginActivity.isLogin) {
            driveInfoBoard.setVisibility(View.INVISIBLE);
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
    public void onHistoryClicked(View v) {
        System.out.println("History clicked");

        Intent intent = new Intent(getApplicationContext(), ActivityRideHistory.class);
        startActivity(intent);

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

        RideEstimateRequest estimateRequest = this.estimateManager.getRideEstimateRequest();
        this.bookingHandler.doBooking(estimateRequest.getOrigin(), estimateRequest.getDestination(), estimateRequest.getCarType(), PROVIDER_UBER);

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

        RideEstimateRequest estimateRequest = this.estimateManager.getRideEstimateRequest();

        this.bookingHandler.doBooking(estimateRequest.getOrigin(), estimateRequest.getDestination(), estimateRequest.getCarType(), PROVIDER_LYFT);
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
                welcomeTextView.setText("Hello, Today is" + System.getProperty ("line.separator") + formattedDate);
                welcomeTextView.setVisibility(View.VISIBLE);

                history.setText("User History");
                history.setVisibility(View.VISIBLE);

                LoginActivity.isLogin = true;
            }
            else {
                welcomeTextView.setText("");
                welcomeTextView.setVisibility(View.INVISIBLE);

                history.setText("");
                history.setVisibility(View.INVISIBLE);
                LoginActivity.isLogin = false;

            }
        }
    }

    public void showBookResponse(BookResponse bookResponse) {
        if (bookResponse == null || !bookResponse.isAccepted()) {
            Toast.makeText(this, "Some error occurs. Please try again latter", Toast.LENGTH_LONG).show();

            return;
        }

        Vehicle v = bookResponse.getVehicle();
        Driver d = bookResponse.getDriver();
        bookingButtons.setVisibility(View.INVISIBLE);

        // show info
        imageLoader.displayImage(v.getImageUrl(), vehicleImage);
        vehiclePlateLicense.setText(v.getLicense());
        vehicleColor.setText(v.getColor() + "-" + v.getYear());
        vehicleInfo.setText(v.getBasicPrudctionInfo());

        imageLoader.displayImage(d.getImageUrl(), driverImage);
        driverName.setText(d.getFirstName());
        driverInfo.setText(d.getPhoneNumber());

        driveInfoBoard.setVisibility(View.VISIBLE);
    }

    public void onRegisterClicked(View v) {
        System.out.println("register clicked");

//        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
        startActivity(intent);

        //initRoseChart();
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
        //mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
        mMap.animateCamera(zoom);

    }

//    private void setUpClustering() {
//        // Declare a variable for the cluster manager.
//        //ClusterManager<clusterItem> mClusterManager;
//
//        // Position the map in UK.
//        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), DEFAULT_ZOOM_LEVEL));
//
//        // Initialize the manager with the context and the map.
//        mClusterManager = new ClusterManager<clusterItem>(this, mMap);
//
//        // Point the map's listeners at the listeners implemented by the cluster manager.
//        mMap.setOnCameraIdleListener(mClusterManager);
//        mMap.setOnMarkerClickListener(mClusterManager);
//
//
//
//        // Add cluster items (markers) to the cluster manager.
//        addMarkers();
//    }
//
//    private void addMarkers(){
//
//        // Set some lat/lng coordinates to start with.
//        double latitude = 37.7764;
//        double longitude = -122.393;
//
//        // Add 100 clusters items in close proximity, for purposes of this example.
//        for (int i = 0; i < 100; i++) {
//            double offset = i / 60d;
//            latitude = latitude + offset;
//            longitude = longitude + offset;
//            clusterItem offsetItem = new clusterItem(latitude, longitude);
//            mClusterManager.addItem(offsetItem);
//        }
//
//    }
//
//    public class RoseChartRenderer extends DefaultClusterRenderer<clusterItem> {
//
//        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
//       private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
////        private final ImageView mImageView;
//        private final ImageView mClusterImageView;
//        private final View viewRoseChart;
////        private final int mDimension;
//
//        public RoseChartRenderer() {
//            super(getApplicationContext(), mMap, mClusterManager);
//
//
//            viewRoseChart = getLayoutInflater().inflate(R.layout.popwindow, null);
//            mClusterIconGenerator.setContentView(viewRoseChart);
//            mClusterImageView = (ImageView) viewRoseChart.findViewById(R.id.image);
//
////            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
////            mClusterIconGenerator.setContentView(multiProfile);
////            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
////
////            mImageView = new ImageView(getApplicationContext());
////            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
////            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
////            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
////            mImageView.setPadding(padding, padding, padding, padding);
////            mIconGenerator.setContentView(mImageView);
//        }
//
//       // @Override
//        protected void onBeforeClusterItemRendered(View roseChart, MarkerOptions markerOptions) {
//
//
////            // Draw a single person.
////            // Set the info window to show their name.
////            mImageView.setImageResource(person.profilePhoto);
////            Bitmap icon = mIconGenerator.makeIcon();
////            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
//        }
//
////        @Override
////        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
////            // Draw multiple people.
////            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
////            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
////            int width = mDimension;
////            int height = mDimension;
////
////            for (Person p : cluster.getItems()) {
////                // Draw 4 at most.
////                if (profilePhotos.size() == 4) break;
////                Drawable drawable = getResources().getDrawable(p.profilePhoto);
////                drawable.setBounds(0, 0, width, height);
////                profilePhotos.add(drawable);
////            }
////            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
////            multiDrawable.setBounds(0, 0, width, height);
////
////            mClusterImageView.setImageDrawable(multiDrawable);
////            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
////            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
////        }
//
//        @Override
//        protected boolean shouldRenderAsCluster(Cluster cluster) {
//            // Always render clusters.
//            return cluster.getSize() > 1;
//        }
//    }
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
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double lat = mLastLocation.getLatitude();
//            double lng = mLastLocation.getLongitude();
//
//            mCurrentLocation =  new LatLng(lat, lng);
//            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You are here"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLocation));
//
//            CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
//            mMap.animateCamera(zoom);
//
//        }
//        else
//        {
//            Toast.makeText(this, "Unable to fetch the current location", Toast.LENGTH_SHORT).show();
//        }

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

        if (this.estimateManager != null) {
            RideEstimateRequest estimateRequest = this.estimateManager.getRideEstimateRequest();
            if (estimateRequest != null) {
                estimateRequest.setCarType(this.getSelectedCarTypeAsString());
            }
        }
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


    /**
     * Draws Rose Chart inside markers (using IconGenerator).
     * When there are multiple Chart in the cluster, draw multiple Chart (using MultiDrawable ).
     */
    private class AssetRenderer extends DefaultClusterRenderer<Asset> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private LinearLayout mLinearLayout;
        private LinearLayout mClusterImageView;
        private int mDimension;


        public AssetRenderer() {
            super(getApplicationContext(), mMap,mClusterManager);

            //fakeOrigin1 = Origin.createMe("Lubbock");
            mDimension = (int)getResources().getDimension(R.dimen.custom_profile_image);
//            mCharts = new NightingaleRoseChart(main, fakeOrigin1);
//            mCharts.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));

            //View multiProfile = getLayoutInflater().inflate(R.layout.popwindow, null);
            //mClusterImageView = (LinearLayout) multiProfile.findViewById(R.id.rose_chart);
        }

        @Override
        protected void onBeforeClusterItemRendered(Asset Asset, MarkerOptions markerOptions) {
            // Draw a single Asset.
            // Set the info window to show their name.
            mCharts = new NightingaleRoseChart(main,Asset.getUber_east(),Asset.getLyft_east(),Asset.getUber_west(),Asset.getLyft_west(),Asset.getUber_north(),Asset.getLyft_north(),Asset.getUber_south(),Asset.getLyft_south());
            mDimension = (int)getResources().getDimension(R.dimen.custom_profile_image);
            mCharts.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));
            mIconGenerator.setContentView(mCharts);

            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(Asset.locationName);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Asset> cluster, MarkerOptions markerOptions) {

            cluster_lat = 0;
            cluster_lng = 0;

            uber_east = 0;
            uber_west = 0;
            uber_south = 0;
            uber_north = 0;

            lyft_east = 0;
            lyft_west = 0;
            lyft_north = 0;
            lyft_south = 0;

            for (Asset p : cluster.getItems()) {

                uber_east = uber_east + p.getUber_east();
                uber_west = uber_west + p.getUber_west();
                uber_south = uber_south + p.getUber_south();
                uber_north = uber_north + p.getUber_north();

                lyft_east = lyft_east + p.getLyft_east();
                lyft_west = lyft_west + p.getLyft_west();
                lyft_north = lyft_north + p.getLyft_north();
                lyft_south = lyft_south + p.getUber_south();
            }

            uber_east = uber_east / cluster.getItems().size();
            uber_west = uber_west / cluster.getItems().size();
            uber_south = uber_south / cluster.getItems().size();
            uber_north = uber_north / cluster.getItems().size();

            lyft_east = lyft_east / cluster.getItems().size();
            lyft_west = lyft_west / cluster.getItems().size();
            lyft_north = lyft_north / cluster.getItems().size();
            lyft_south = lyft_south / cluster.getItems().size();

            mClusterCharts = new NightingaleRoseChart(main,uber_east,lyft_east,uber_west,lyft_west,uber_north,lyft_north,uber_south,lyft_south);
            mDimension = (int)getResources().getDimension(R.dimen.custom_profile_image);
            mClusterCharts.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));
            mClusterIconGenerator.setContentView(mClusterCharts);

            Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(String.valueOf(cluster.getItems().size()));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }
    @Override
    public boolean onClusterClick(Cluster<Asset> cluster) {

        uber_east = 0;
        uber_west = 0;
        uber_south = 0;
        uber_north = 0;

        lyft_east = 0;
        lyft_west = 0;
        lyft_north = 0;
        lyft_south = 0;

        for (Asset p : cluster.getItems()) {

            uber_east = uber_east + p.getUber_east();
            uber_west = uber_west + p.getUber_west();
            uber_south = uber_south + p.getUber_south();
            uber_north = uber_north + p.getUber_north();

            lyft_east = lyft_east + p.getLyft_east();
            lyft_west = lyft_west + p.getLyft_west();
            lyft_north = lyft_north + p.getLyft_north();
            lyft_south = lyft_south + p.getUber_south();
        }

        uber_east = uber_east / cluster.getItems().size();
        uber_west = uber_west / cluster.getItems().size();
        uber_south = uber_south / cluster.getItems().size();
        uber_north = uber_north / cluster.getItems().size();

        lyft_east = lyft_east / cluster.getItems().size();
        lyft_west = lyft_west / cluster.getItems().size();
        lyft_north = lyft_north / cluster.getItems().size();
        lyft_south = lyft_south / cluster.getItems().size();

        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uber_east",uber_east);
            jsonObject.put("uber_west",uber_west);
            jsonObject.put("uber_south",uber_south);
            jsonObject.put("uber_north",uber_north);

            jsonObject.put("lyft_east",lyft_east);
            jsonObject.put("lyft_west",lyft_west);
            jsonObject.put("lyft_north",lyft_north);
            jsonObject.put("lyft_south",lyft_south);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = jsonObject.toString();
        intent.putExtra("cluster", str);
        startActivity(intent);
//
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Asset> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Asset asset) {
        uber_east = 0;
        uber_west = 0;
        uber_south = 0;
        uber_north = 0;

        lyft_east = 0;
        lyft_west = 0;
        lyft_north = 0;
        lyft_south = 0;

        uber_east = asset.getUber_east();
        uber_west = asset.getUber_west();
        uber_south = asset.getUber_south();
        uber_north = asset.getUber_north();

        lyft_east = asset.getLyft_east();
        lyft_west = asset.getLyft_west();
        lyft_north = asset.getLyft_north();
        lyft_south = asset.getLyft_south()
        ;
        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uber_east",uber_east);
            jsonObject.put("uber_west",uber_west);
            jsonObject.put("uber_south",uber_south);
            jsonObject.put("uber_north",uber_north);

            jsonObject.put("lyft_east",lyft_east);
            jsonObject.put("lyft_west",lyft_west);
            jsonObject.put("lyft_north",lyft_north);
            jsonObject.put("lyft_south",lyft_south);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = jsonObject.toString();
        intent.putExtra("cluster", str);
        startActivity(intent);

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Asset asset) {

    }

    protected void startDemo() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.988612, 53.459411), 3.5f));

        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new AssetRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();
    }

    private void addItems() {

        for (int i = 0; i < 100; i++) {
            mClusterManager.addItem(new Asset(randomLocation(), position()));
        }
    }
    private LatLng position() {
        return new LatLng(random(30.325877, 36.615905), random(60.648976, 47.465382));
    }
    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
    private String randomLocation() {
        int i = (int)Math.random() * 100000;

        return "Test-" + i;
    }
//    private int randomImage() {
//        return (int)Math.random() * 10;
//    }


}


