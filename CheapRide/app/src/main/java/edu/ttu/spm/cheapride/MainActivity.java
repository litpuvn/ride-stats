package edu.ttu.spm.cheapride;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import edu.ttu.spm.cheapride.handler.BookingHandler;
import edu.ttu.spm.cheapride.handler.EstimateHandler;
import edu.ttu.spm.cheapride.model.BookResponse;
//import edu.ttu.spm.cheapride.model.ClusteringDemoActivity;
import edu.ttu.spm.cheapride.model.HistoryRecordEntity;
import edu.ttu.spm.cheapride.model.NightingaleRoseChart;
import edu.ttu.spm.cheapride.model.RideEstimate;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;
import edu.ttu.spm.cheapride.model.RideEstimateRequest;
import edu.ttu.spm.cheapride.model.item.Asset;
import edu.ttu.spm.cheapride.model.item.Driver;
import edu.ttu.spm.cheapride.model.item.Origin;
import edu.ttu.spm.cheapride.model.item.Vehicle;
import edu.ttu.spm.cheapride.service.TrackGPS;
import edu.ttu.spm.cheapride.view.DemoView;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.Cluster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import static edu.ttu.spm.cheapride.AbstractNetworkRequest.CONNECTION_TIMEOUT;
import static edu.ttu.spm.cheapride.AbstractNetworkRequest.READ_TIMEOUT;


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
    private int DIALOG_ID = 0;
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

    private double totalLat = 0;
    private double totalLng = 0;

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

    private NightingaleRoseChart mCharts;
    private NightingaleRoseChart mClusterCharts;
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

    String title = "";
    LatLng position;

    private SeekBar seekBar;
    private TextView textView_seekBar;

    private MainActivity.UserSetputTime mRideCostComparisonTask = null;

    private List<Asset> assetList = new ArrayList<>();

    int year_picker;
    int month_picker;
    int day_picker;
    private Button date_submit;

    String showDate = null;

    double lat = 0;
    double lon = 0;

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


//        autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

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
        textView_seekBar = (EditText)findViewById(R.id.textView_seekBar);


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

        setDefaultDate();

        showDialogOnTextViewClick();

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
        initTimeSeekBar();

        initRideEstimationCluster();

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

        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);

//        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
//        startActivity(intent);

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
            title = Asset.getLocationName();
//            position = Asset.getPosition()''
            mCharts = new NightingaleRoseChart(main,Asset.getUber_east(),Asset.getLyft_east(),Asset.getUber_west(),Asset.getLyft_west(),Asset.getUber_north(),Asset.getLyft_north(),Asset.getUber_south(),Asset.getLyft_south());
            mDimension = (int)getResources().getDimension(R.dimen.custom_profile_image);
            mCharts.setLayoutParams(new ViewGroup.LayoutParams(mDimension,mDimension));
            mCharts.initView();

            mIconGenerator.setContentView(mCharts);

            Bitmap icon = mIconGenerator.makeIcon(Asset.locationName);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Asset> cluster, MarkerOptions markerOptions) {

            //title = String.valueOf(cluster.getSize()) + " Items";

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

            mClusterCharts.initView();
            mClusterIconGenerator.setContentView(mClusterCharts);

            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
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

        //title = String.valueOf(cluster.getSize()) + " Items";

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
            jsonObject.put("title",title);
            jsonObject.put("lat",lat);
            jsonObject.put("lon",lon);
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
        title = null;
        uber_east = 0;
        uber_west = 0;
        uber_south = 0;
        uber_north = 0;

        lyft_east = 0;
        lyft_west = 0;
        lyft_north = 0;
        lyft_south = 0;

        title = asset.getLocationName();
        position = asset.getPosition();


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
            jsonObject.put("title",title);
            jsonObject.put("lat",position.latitude);
            jsonObject.put("lon",position.longitude);
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

    protected void initRideEstimationCluster() {
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new AssetRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);


    }

    private void startClusters() {
        addItems();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(totalLat / assetList.size(), totalLng / assetList.size()), 3.5f));


        mClusterManager.cluster();
    }

    private void addItems() {
        if (assetList != null && assetList.size() > 0) {
            Asset tmp;
            totalLat = 0;
            totalLng = 0;

            for (int i = 0; i < assetList.size(); i++) {
                tmp = assetList.get(i);
                totalLat += tmp.getLat();
                totalLng += tmp.getLng();
                mClusterManager.addItem(tmp);
            }
        }
    }
    private LatLng position() {
        return new LatLng(random(30.325877, 36.615905), random(60.648976, 47.465382));
    }
    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
    private String randomLocation() {
        double i = Math.random() * 10000;
        int i1 = (int)i;

        return "Test-" + i1;
    }

    public void initTimeSeekBar(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int mins = c.get(Calendar.MINUTE);

        //seekBar.getTickMark();
        //seekBar.setProgress();

        seekBar = (SeekBar)findViewById(R.id.seek_Bar);
        textView_seekBar.setText("Selected Time :  " + showDate +  "    " + convertTime(seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                textView_seekBar.setText("Selected Time :  " + showDate + "    " + convertTime(progress));
                //textView_seekBar.setTextSize(20);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_seekBar.setText("Selected Time :  " + showDate +  "    " + convertTime(progress_value));
                //textView_seekBar.setTextSize(20);
                mMap.clear();

                mRideCostComparisonTask = new UserSetputTime(convertTime(progress_value));
                mRideCostComparisonTask.execute((Void) null);



            }
        });
    }

    public String convertTime(int progress){
        String time = null;
        double totalSec = 864 * progress;
        String hoursS = null;
        String minsS = null;
        String secsS = null;

        int hours = (int)(totalSec / 3600);
        int remainder = (int)(totalSec - hours * 3600);
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        if(hours < 10)
            hoursS = "0" + String.valueOf(hours);
        else
            hoursS = String.valueOf(hours);

        if(mins < 10)
            minsS = "0" + String.valueOf(mins);
        else
            minsS = String.valueOf(mins);

        if (secs < 10)
            secsS = "0" + String.valueOf(secs);
        else
            secsS = String.valueOf(secs);

        time = hoursS + ":" + minsS + ":" + secsS;
        return time;
    }

    /**
     * Represents an asynchronous user history task used to authenticate
     * the user.
     */
    public class UserSetputTime extends AsyncTask<Void, Void, Boolean> {

        private  String mTime;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        UserSetputTime(String time) {
            mTime = time;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
//            String serverUrl = MainActivity.BASE_URL + "/popularEstimation?date=" + formattedDate + " " + mTime;
            String serverUrl = "http://129.118.162.163:8080/popularEstimation?date=" + formattedDate + " " + mTime;

            // TODO: submit the request here.
            return performGetCall(serverUrl);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRideCostComparisonTask = null;

            if (success) {
//                Toast.makeText(MainActivity.this, "loading data", Toast.LENGTH_SHORT).show();

                startClusters();

            } else {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mRideCostComparisonTask = null;
        }



        public boolean performGetCall(String requestURL) {

            URL url;
            // ArrayList<HistoryRecordEntity> historyRecordEntityArrayList = new ArrayList<HistoryRecordEntity>();

            try {
                System.out.println("set time: " + requestURL);
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));



                    String inputLine;
                    Origin tmp;

                    assetList.clear();

                    while ((inputLine = reader.readLine()) != null) {
                        JSONArray ja = new JSONArray(inputLine);

                        for(int i = 0; i < ja.length(); i++){
                            JSONObject jo = (JSONObject) ja.get(i);
                            tmp = Origin.createFromJsonObject(jo);

                            assetList.add(Asset.createFromOrigin(tmp));
                        }
                    }

                } else {
                    Log.e(TAG, "14 - False - HTTP_OK");
                    assetList = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return assetList.size() > 0;
        }
    }

    public void setDefaultDate(){

        //get current date
        final java.util.Calendar cal = java.util.Calendar.getInstance();

        year_picker = cal.get(java.util.Calendar.YEAR);
        month_picker = cal.get(java.util.Calendar.MONTH) + 1;
        day_picker = cal.get(java.util.Calendar.DAY_OF_MONTH);
        showDate= year_picker + "/" + month_picker + "/" + day_picker;

        textView_seekBar.setText("Selected Time :  " + showDate);

    }

    public void showDialogOnTextViewClick(){

        textView_seekBar.setInputType(InputType.TYPE_NULL);

        textView_seekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIALOG_ID = 1;
                showDialog(DIALOG_ID);
            }
        });

    }

    //@Override
    protected Dialog onCreateDialog(int id){
        if (id == 1)
            return new DatePickerDialog(this,datePickerListener,year_picker,month_picker,day_picker);
        else
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(DIALOG_ID == 1) {
                year_picker = year;
                month_picker = month + 1;
                day_picker = day;
                showDate= year_picker + "/" + month_picker + "/" + day_picker;

                //show date on the text view
                textView_seekBar.setText("Selected Time :  " + showDate);
                return;
            }
            else {
                Toast.makeText(MainActivity.this, "Date Picker Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

}


