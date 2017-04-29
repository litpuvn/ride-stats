package edu.ttu.spm.cheapride.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import edu.ttu.spm.cheapride.AbstractNetworkRequest;
import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.RideEstimateDTO;

public class BookingHandler extends AbstractNetworkRequest {

    private static final String BOOKING_URL = MainActivity.BASE_URL + "/book";
    private final String TAG = "EstimateHandler";

    private RideBookingTask rideBookingTask;
    private Boolean bookingInProgress = false;

    public BookingHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void doBooking(String rideRequestId) {
        if (rideRequestId == null || rideRequestId.length() < 1) {
            Log.i(TAG, "No ride request available");
            return;
        }

        bookingInProgress = true;
        showProgress(true);
        rideBookingTask = new RideBookingTask(rideRequestId);
        rideBookingTask.execute((Void) null);
    }

    @Override
    public String performPostCall(String requestURL, HashMap<Object, Object> postDataParams) {
        return null;
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

        RideBookingTask(String rideRequestId) {
            this.rideRequestId = rideRequestId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<Object, Object> postParams = new HashMap<>();

//            postParams.put("origin", origin);
//            postParams.put("destination", destination);

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
                main.activateComparisonChart(rideEstimateDto);


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
