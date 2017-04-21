package edu.ttu.spm.cheapride;

import android.content.Context;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.ttu.spm.cheapride.model.RideEstimateDTO;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class CheapRideUnitTest {


    @Mock
    private AutoCompleteTextView mEmailView;
    @Mock
    private EditText mPasswordView = Mockito.mock(EditText.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
//    private EditText mPasswordView = Mockito.mock(EditText.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    @Mock
    Context mMockContext;
    @Mock
    LoginActivity mLogin;

    @Test
    public void testEmailValid() {
        String validMail = "cheapride@ttu.edu";
        LoginActivity login = new LoginActivity();
        boolean valid = login.isEmailValid(validMail);
        assertTrue(valid);
    }

    @Test
    public void testEmailNotValid() {
        String validMail = "cheapride@ttuedu";

        LoginActivity login = new LoginActivity();
        assertFalse(login.isEmailValid(validMail));
    }

    @Test
    public void testRideEstimateValid() {

        try {
            MainActivity main = new MainActivity();
            JSONObject response = new JSONObject();
            JSONObject uber = this.createRideEstimate();

            JSONObject lyft = this.createRideEstimate();

            response.put("uber", uber);
            response.put("lyft", lyft);

            RideEstimateDTO ride = RideEstimateDTO.createFromJson(response);
            main.getLyftCostWidth(ride);
            main.getUberCostWidth(ride);
            main.getLyftTimeWidth(ride);
            main.getUberTimeWidth(ride);


            assertTrue(true);
        }
        catch (Exception j) {
            assertFalse(true);
        }
    }

    @Test
    public void testRideEstimateUberValidLyftNull() {

        try {
            MainActivity main = new MainActivity();
            JSONObject response = new JSONObject();
            JSONObject uber = this.createRideEstimate();


            response.put("uber", uber);


            RideEstimateDTO ride = RideEstimateDTO.createFromJson(response);
            main.getLyftCostWidth(ride);
            main.getUberCostWidth(ride);
            main.getLyftTimeWidth(ride);
            main.getUberTimeWidth(ride);


            assertTrue(true);
        }
        catch (Exception j) {
            assertFalse(true);
        }
    }

    @Test
    public void testRideEstimateUberNullLyftValid() {

        try {
            MainActivity main = new MainActivity();
            JSONObject response = new JSONObject();
            JSONObject lyft = this.createRideEstimate();


            response.put("lyft", lyft);


            RideEstimateDTO ride = RideEstimateDTO.createFromJson(response);
            main.getLyftCostWidth(ride);
            main.getUberCostWidth(ride);
            main.getLyftTimeWidth(ride);
            main.getUberTimeWidth(ride);


            assertTrue(true);
        }
        catch (Exception j) {
            assertFalse(true);
        }
    }

    private JSONObject createRideEstimate() {
        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("rideRequestId","request-id-" + Math.random() * 1000);
            json.put("time", Math.random() * 200);
            json.put("cost", 5 + Math.random() * 20);
        }
        catch (JSONException j) {

        }

        return json;
    }

}