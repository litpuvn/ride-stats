package edu.ttu.spm.cheapride;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class EstimateManager extends AbstractNetworkRequest {
    private static final String RIDE_ESTIMATE_URL = MainActivity.BASE_URL + "/estimate";
    private final String TAG = "EstimateManager";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int READ_TIMEOUT = 30000; // seconds
    private static final int CONNECTION_TIMEOUT = 30000; // seconds

    private RequestEstimateTask requestEstimateTask;

    public void attemptEstimate(Location origin, Location destination) {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        requestEstimateTask = new RequestEstimateTask(origin, destination);
        requestEstimateTask.execute((Void) null);
    }


    @Override
    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        return null;
    }

    @Override
    public void showProgress(boolean show) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RequestEstimateTask extends AsyncTask<Void, Void, Boolean> {

        private Location origin;
        private Location destination;

        RequestEstimateTask(Location origin, Location destination) {
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, Location> postParams = new HashMap<>();

            postParams.put("origin", origin);
            postParams.put("destination", destination);

            return performPostCall(RIDE_ESTIMATE_URL, postParams).length() > 0;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//
//                LoginActivity loginActivity = (LoginActivity) context;
//                Intent resultIntent = new Intent();
//                String response = loginResponse != null ? loginResponse.toString() : null;
//                resultIntent.putExtra("response", response);
//                loginActivity.setResult(RESULT_OK, resultIntent);
//                finish();
//
//
//            } else {
//                mPasswordView.setError(getString(R.string.error_not_authorized_access));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }



        public String performPostCall(String requestURL,
                                      HashMap<String, Location> postDataParams) {

            URL url;
            String responseStr = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                Log.e(TAG, "11 - url : " + requestURL);

            /*
             * JSON
             */

                JSONObject root = new JSONObject();
                JSONObject originJson = new JSONObject();
                originJson.put("lat", origin.getLatitude());
                originJson.put("lon", origin.getLongitude());

                JSONObject destinationJson = new JSONObject();
                destinationJson.put("lat", destination.getLatitude());
                destinationJson.put("lon", destination.getLongitude());

                root.put("origin", originJson);
                root.put("destination", destinationJson);

                Log.e(TAG, "ride request : " + root.toString());

                String str = root.toString();
                byte[] outputBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);

                int responseCode = conn.getResponseCode();

                Log.e(TAG, "responseCode : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e(TAG, "HTTP_OK");

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
    }
}
