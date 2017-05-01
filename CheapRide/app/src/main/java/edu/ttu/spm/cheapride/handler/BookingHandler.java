package edu.ttu.spm.cheapride.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import edu.ttu.spm.cheapride.AbstractNetworkRequest;
import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.BookResponse;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;

public class BookingHandler extends AbstractNetworkRequest {

    private static final String BOOKING_URL = MainActivity.BASE_URL + "/bookRide";
    private final String TAG = "BookingHandler";

    private RideBookingTask rideBookingTask;
    private Boolean bookingInProgress = false;

    public BookingHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void doBooking(String rideRequestId, String provider) {
        if (rideRequestId == null || rideRequestId.length() < 1) {
            Log.i(TAG, "No ride request available");
            return;
        }

        bookingInProgress = true;
        showProgress(true);
        rideBookingTask = new RideBookingTask(rideRequestId, provider);
        rideBookingTask.execute((Void) null);
    }

    public String performPostCall(String requestURL, HashMap<Object, Object> params) {

        String proivder = (String)params.get("provider");
        String rideRequestId = (String)params.get("rideRequestId");

        MainActivity m = (MainActivity)mContext;

        String requestStr = requestURL + "?rideRequestId=" + rideRequestId + "&provider=" + proivder;
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

    public boolean isBooking() {
        return bookingInProgress == true;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RideBookingTask extends AsyncTask<Void, Void, Boolean> {
        private String rideRequestId;
        private String provider;

        RideBookingTask(String rideRequestId, String provider) {
            this.rideRequestId = rideRequestId;
            this.provider = provider;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<Object, Object> postParams = new HashMap<>();

            postParams.put("rideRequestId", this.rideRequestId);
            postParams.put("provider", this.provider);

            return performPostCall(BOOKING_URL, postParams).length() > 0;
//            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
            if (success) {
                MainActivity main = (MainActivity)mContext;
                BookResponse bookResponse = BookResponse.createFromJson(response);
                main.showBookResponse(bookResponse);


            } else {

            }

            bookingInProgress = false;
        }



        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }

    }
}
