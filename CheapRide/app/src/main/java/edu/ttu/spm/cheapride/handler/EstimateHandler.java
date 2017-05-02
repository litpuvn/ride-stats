package edu.ttu.spm.cheapride.handler;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import edu.ttu.spm.cheapride.AbstractNetworkRequest;
import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.RideEstimate;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;
import edu.ttu.spm.cheapride.model.RideEstimateRequest;

public class EstimateHandler extends AbstractNetworkRequest {
    private static final String RIDE_ESTIMATE_URL = MainActivity.BASE_URL + "/getEstimate";
    private final String TAG = "EstimateHandler";

    private RideEstimateDTO rideEstimateResponse;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int READ_TIMEOUT = 30000; // seconds
    private static final int CONNECTION_TIMEOUT = 30000; // seconds

    public EstimateHandler(Context mContext) {
        this.mContext = mContext;
    }

    private RequestEstimateTask requestEstimateTask;
    private RideEstimateRequest rideEstimateRequest;

    public void attemptEstimate(LatLng origin, LatLng destination, String carType) {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        rideEstimateRequest = new RideEstimateRequest(origin, destination, carType);
        requestEstimateTask = new RequestEstimateTask(rideEstimateRequest);
        requestEstimateTask.execute((Void) null);
    }

    public String performPostCall(String requestURL, HashMap<Object, Object> params) {
        LatLng pickup = (LatLng)params.get("origin");
        LatLng destination = (LatLng)params.get("destination");
        String carType = (String)params.get("carType");

        String requestStr = requestURL + "?pick_up_lattitude=" + pickup.latitude + "&pick_up_longitude=" + pickup.longitude +
                "&drop_off_lattitude=" +  destination.latitude  + "&drop_off_longitude=" +  destination.longitude + "&car_type=" +carType;
//        String requestStr = MainActivity.BASE_URL + "/getEstimate?pick_up_lattitude=37.7753&pick_up_longitude=-122.418&drop_off_lattitude=37.787654&drop_off_longitude=-122.40276";
        String responseStr = "";
        URL url;
        System.out.println("Request: " + requestStr);
        try {
            url = new URL(requestStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("token", "test");

            Log.e(TAG, "11 - url : " + requestURL);
            int responseCode = conn.getResponseCode();
            System.out.println("Request URL: " + requestStr);
            Log.e(TAG, "13 - responseCode : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.e(TAG, "14 - HTTP_OK");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    responseStr += line;
                }

                response = new JSONObject(responseStr);

            } else {
                Log.e(TAG, "14 - False - HTTP_OK");
                responseStr = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseStr;
    }

    @Override
    public void showProgress(boolean show) {

    }

    public RideEstimateDTO getRideEstimateResponse() {
        return this.rideEstimateResponse;
    }

    public RideEstimateRequest getRideEstimateRequest() {
        return rideEstimateRequest;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RequestEstimateTask extends AsyncTask<Void, Void, Boolean> {

        private RideEstimateRequest request;

        RequestEstimateTask(RideEstimateRequest request) {
            this.request = request;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<Object, Object> postParams = new HashMap<>();

            postParams.put("origin", this.request.getOrigin());
            postParams.put("destination", this.request.getDestination());
            postParams.put("carType", this.request.getCarType());

//            return performPostCall(RIDE_ESTIMATE_URL, postParams).length() > 0;
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
            if (success) {
                MainActivity main = (MainActivity)mContext;
                RideEstimateDTO rideEstimateDto = RideEstimateDTO.createFromJson(response);
                rideEstimateResponse = rideEstimateDto;
                main.activateComparisonChart(rideEstimateDto);

            } else {

            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }

    }
}
