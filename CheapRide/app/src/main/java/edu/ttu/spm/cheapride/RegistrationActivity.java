package edu.ttu.spm.cheapride;

import android.content.Intent;;
import android.support.v7.app.AppCompatActivity;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import android.os.StrictMode;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private EditText register_email;
    private EditText register_password;
    private EditText reregister_password;
    private Button register_submit;
    private Button register_cancel;

    private RegistrationActivity.UserRegisterTask mAuthTask = null;
    private static final int READ_TIMEOUT = 30000; // seconds
    private static final int CONNECTION_TIMEOUT = 30000; // seconds
    private final String TAG = "post json example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_registration);

        register_email=(EditText)findViewById(R.id.email_registration);
        register_password=(EditText)findViewById(R.id.password_registration);
        reregister_password=(EditText)findViewById(R.id.retype_password_registration);
        register_submit=(Button)findViewById(R.id.submit_registration);
        register_cancel=(Button)findViewById(R.id.cancel_registration);

        register_email.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    if(register_email.getText().toString().trim().equals("")){
                        Toast.makeText(RegistrationActivity.this, "Email can not be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if(register_email.getText().toString().trim().length()<4){
                        Toast.makeText(RegistrationActivity.this, "Email address too short", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isEmailValid(register_email.getText().toString())){
                        Toast.makeText(RegistrationActivity.this, "Email is not validation", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        register_password.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    if(register_password.getText().toString().trim().length()<8){
                        Toast.makeText(RegistrationActivity.this, "Password can not shorter than 8 letters", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isPasswordValid(register_password.getText().toString())){
                        Toast.makeText(RegistrationActivity.this, "Password must includes 1 number and 1 special", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        reregister_password.setOnFocusChangeListener(new OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(!hasFocus){
                    if(!reregister_password.getText().toString().trim().equals(register_password.getText().toString().trim())){
                        Toast.makeText(RegistrationActivity.this, "Confirm password not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        register_submit.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!checkEdit()){
                    return;
                }
                // TODO Auto-generated method stub
                String email = register_email.getText().toString();
                String password = register_password.getText().toString();
                String rePassword = reregister_password.getText().toString();

                mAuthTask = new UserRegisterTask(email, password,rePassword);
                mAuthTask.execute((Void) null);
            }


        });

        register_cancel.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("go back to main screen");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

    }
    //check the Email validation
    private boolean isEmailValid(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        return false;
    }
    //check the password validation
    public boolean isPasswordValid(String password) {

        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");

    }
    private boolean checkEdit(){
        if(register_email.getText().toString().trim().equals("")){
            Toast.makeText(RegistrationActivity.this, "Email can not be empty", Toast.LENGTH_SHORT).show();
        }else if(register_password.getText().toString().trim().equals("")){
            Toast.makeText(RegistrationActivity.this, "Password can not be empty", Toast.LENGTH_SHORT).show();
        }else if(!register_password.getText().toString().trim().equals(reregister_password.getText().toString().trim())){
            Toast.makeText(RegistrationActivity.this, "Confirm password not match", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        //private final String mRePassword;

        UserRegisterTask(String email, String password, String rePassword) {
            mEmail = email;
            mPassword = password;
            //mRePassword = rePassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = MainActivity.BASE_URL + "/register";
            HashMap<String, String> postParams = new HashMap<>();

            postParams.put("username", mEmail);
            postParams.put("password", mPassword);
            //postParams.put("rePassword", mRePassword);

            //performPostCall(serverUrl, postParams);

            // TODO: register the new account here.
            return performPostCall(serverUrl, postParams).length() > 0;
            //return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Toast.makeText(RegistrationActivity.this, "Registration success", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                register_email.setError("Your Email has been used");
                register_email.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }



        public String performPostCall(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                System.out.println("register request: " + requestURL);
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
                root.put("username", postDataParams.get("username"));
                root.put("password", postDataParams.get("password"));
                //root.put("rePassword", postDataParams.get("rePassword"));

                Log.e(TAG, "12 - root : " + root.toString());

                String str = root.toString();
                byte[] outputBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);

                int responseCode = conn.getResponseCode();

                Log.e(TAG, "13 - responseCode : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                    Log.e(TAG, "14 - HTTP_OK");

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
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