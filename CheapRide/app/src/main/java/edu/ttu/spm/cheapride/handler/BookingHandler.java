package edu.ttu.spm.cheapride.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import edu.ttu.spm.cheapride.AbstractNetworkRequest;
import edu.ttu.spm.cheapride.LoginActivity;
import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.BookRequest;
import edu.ttu.spm.cheapride.model.BookResponse;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;
import edu.ttu.spm.cheapride.model.item.GeoLoc;

public class BookingHandler extends AbstractNetworkRequest {

    private static final String BOOKING_URL = MainActivity.BASE_URL + "/bookRide";
    private final String TAG = "BookingHandler";

    private RideBookingTask rideBookingTask;
    private SetHistoryDateTask setHistoryTask;
    private Boolean bookingInProgress = false;

    public BookingHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void doBooking(LatLng origin, LatLng destination, String carType, String provider) {

        BookRequest bR = new BookRequest(provider, carType, new GeoLoc(origin.latitude, origin.longitude), new GeoLoc(destination.latitude, destination.longitude));

        bookingInProgress = true;
        showProgress(true);
        rideBookingTask = new RideBookingTask(bR);
        rideBookingTask.execute((Void) null);
    }

    public String performPostCall(String requestURL, HashMap<Object, Object> params) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            Log.i(TAG, "11 - url : " + requestURL);

            /*
             * JSON
             */

            BookRequest req = (BookRequest)params.get("request");
            Log.i(TAG, req.toJson().toString());

            JSONObject root = req.toJson();

            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);

            int responseCode = conn.getResponseCode();

            Log.e(TAG, "13 - responseCode : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.e(TAG, "14 - HTTP_OK");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }

                this.response = new JSONObject(response);

            } else {
                Log.e(TAG, "14 - False - HTTP_OK");
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
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
        private BookRequest bookRequest;

        RideBookingTask(BookRequest bookRequest) {

            this.bookRequest = bookRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<Object, Object> postParams = new HashMap<>();

            postParams.put("request", this.bookRequest);

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
                RideEstimateDTO est = main.getEstimateManager().getRideEstimateResponse();
                double cost = bookRequest.getProvider() == MainActivity.PROVIDER_UBER ? est.getUberCost() : est.getLyftCost();

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String currentDateAndTime = sdf.format(new Date());
                System.out.println("today is: " + currentDateAndTime);

                setHistoryTask = new SetHistoryDateTask(LoginActivity.email, currentDateAndTime, bookRequest.getOriginAsString(), bookRequest.getDestinationAsString(), String.valueOf(cost), bookRequest.getProvider());
                setHistoryTask.execute((Void) null);

            } else {
                Toast.makeText(mContext, "Some error occurs. Please try again latter", Toast.LENGTH_LONG).show();
            }

            bookingInProgress = false;
        }



        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }

        public String getDate(long milliSecond, String dateFormat){
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSecond);
            return formatter.format(calendar.getTime());
        }

    }



    public class SetHistoryDateTask extends AsyncTask<Void, Void, Boolean> {

        private  String mUserName;
        private  String mDate;
        private  String mPick;
        private  String mDestination;
        private  String mFee;
        private  String mProvider;


        SetHistoryDateTask(String userName, String date, String pick, String destination, String fee, String provider) {
            mUserName = userName;
            mDate = date;
            mPick = pick;
            mDestination = destination;
            mFee = fee;
            mProvider = provider;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = MainActivity.BASE_URL + "/setHistory";
            HashMap<String, String> postParams = new HashMap<>();

            postParams.put("username", mUserName);
            postParams.put("date", mDate);
            postParams.put("pickup",mPick);
            postParams.put("destination",mDestination);
            postParams.put("fee",mFee);
            postParams.put("provider",mProvider);

            return setHistoryCall(serverUrl, postParams).length() > 0;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }



        protected String setHistoryCall(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                System.out.println("set history request: " + requestURL);
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                Log.e(TAG, "11 - url : " + requestURL);

                JSONObject root = new JSONObject();
                root.put("username", postDataParams.get("username"));
                root.put("date", postDataParams.get("date"));
                root.put("pickup", postDataParams.get("pickup"));
                root.put("destination", postDataParams.get("destination"));
                root.put("fee", postDataParams.get("fee"));
                root.put("provider", postDataParams.get("provider"));

                Log.e(TAG, "12 - root : " + root.toString());

                String str = root.toString();
                byte[] outputBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);

                int responseCode = conn.getResponseCode();

                Log.e(TAG, "13 - responseCode : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e(TAG, "14 - HTTP_OK");

                    Log.i(TAG, conn.getResponseMessage());
                } else {
                    Log.e(TAG, "14 - False - HTTP_OK");
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
