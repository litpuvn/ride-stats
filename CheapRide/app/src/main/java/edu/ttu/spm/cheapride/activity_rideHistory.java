package edu.ttu.spm.cheapride;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static edu.ttu.spm.cheapride.R.attr.layoutManager;


public class activity_rideHistory extends AppCompatActivity {

    private EditText startDate;
    private EditText endDate;
    int year_start;
    int month_start;
    int day_start;
    int year_end;
    int month_end;
    int day_end;
    String showStartTime = null;
    String showEndTime = null;

    String userName = "john";
    int DIALOG_ID = 0;
    private Button date_submit;
    private pageable page;
    int pageSize = 10;
    TextView pageCounter;
    Button nextPage;
    Button previousPage;



//    JSONArray historyResponse;
//    JSONObject json;

    private activity_rideHistory.UserSelectDateTask mAuthTask = null;
    private activity_rideHistory.SetHistoryDateTask mTestTask = null;
    private static final int READ_TIMEOUT = 30000; // seconds
    private static final int CONNECTION_TIMEOUT = 30000; // seconds
    private final String TAG = "post json example";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CustomAdapter adapter;
    private List<historyRecordEntity> historyRecordArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        page = new pageable(1,4);

        //set default date
        setDefaultDate();

        //show the date picker
        showDialogOnTextViewClick();

        //insert history record cards
        historyRecyclerView();

    }

    public void setDefaultDate(){

        //get current date
        final Calendar cal = Calendar.getInstance();

        year_start = cal.get(Calendar.YEAR);
        month_start = cal.get(Calendar.MONTH);
        day_start = cal.get(Calendar.DAY_OF_MONTH);

        year_end = cal.get(Calendar.YEAR);
        month_end = cal.get(Calendar.MONTH);
        day_end = cal.get(Calendar.DAY_OF_MONTH);

    }

    public void showDialogOnTextViewClick(){
        startDate = (EditText)findViewById(R.id.startDate_history);
        endDate = (EditText)findViewById(R.id.endDate_history);
        date_submit = (Button) findViewById(R.id.submit_history);

        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIALOG_ID = 1;
                showDialog(DIALOG_ID);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIALOG_ID = 2;
                showDialog(DIALOG_ID);
            }
        });

        date_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //submit some fake data to test
//                for(int i = 0; i <= 100; i++) {
//                    //TODO Auto-generated method stub
//                    String username = "john";
//                    String date = "09/09/2017";
//                    String pick = "broadway avenue";
//                    String destination = "University avenue";
//                    String fee = "$20";
//                    String provider = "uber";
//
//                    mTestTask = new SetHistoryDateTask(username, date, pick, destination, fee, provider);
//                    mTestTask.execute((Void) null);
//                }

                //submit the request to get history data
                if(showStartTime == null){
                    Toast.makeText(activity_rideHistory.this, "please enter from date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(showEndTime == null){
                    Toast.makeText(activity_rideHistory.this, "please enter to date", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    page.setPage(1);
                    pageCounter  = (TextView) findViewById(R.id.pageCounter_history);
                    pageCounter.setTextSize(30);
                    pageCounter.setText("Page" + page.getPage());
                    displayCard(page.getPage());
                }
            }
        });
    }

    //@Override
    protected Dialog onCreateDialog(int id){
        if (id == 1)
            return new DatePickerDialog(this,datePickerListener,year_start,month_start,day_start);
        if (id ==2)
            return new DatePickerDialog(this,datePickerListener,year_end,month_end,day_end);
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(DIALOG_ID == 1) {
                year_start = year;
                month_start = month + 1;
                day_start = day;
                showStartTime = month_start + "/" + day_start + "/" + year_start;

                //show date on the text view
                startDate.setText(showStartTime);
                return;
            }
            if(DIALOG_ID ==2){
                year_end = year;
                month_end = month + 1;
                day_end = day;
                showEndTime = month_end + "/" + day_end + "/" + year_end;

                //show date on the text view
                endDate.setText(showEndTime);
                return;
            }
            else {
                Toast.makeText(activity_rideHistory.this, "Date Picker Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };


    /**
     * Represents an asynchronous user history task used to authenticate
     * the user.
     */
    public class UserSelectDateTask extends AsyncTask<Void, Void, Boolean> {

        private  String mUserName;
        private  String mStartDate;
        private  String mEndDate;
        private int mPage;
        private int mPageSize;


        UserSelectDateTask(String userName, String startDate, String endDate, int page, int pageSize) {
            mUserName = userName;
            mStartDate = startDate;
            mEndDate = endDate;
            mPage = page;
            mPageSize = pageSize;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //String serverUrl = MainActivity.BASE_URL + "/getHistoryByDate?" + "username=" + mUserName + "&fromDate=" + mStartDate + "&toDate=" + mEndDate + "&pageNumber=" + mPage + "&size=" + mPageSize;
            String serverUrl = MainActivity.BASE_URL + "/getHistoryByDate?" + "username=" + mUserName + "&from=" + "5/5/2010" + "&to=" + "5/5/2018" + "&pageNumber=" + mPage + "&size=" + mPageSize;

            // TODO: submit the request here.
            return performGetCall(serverUrl)!=null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                adapter = new CustomAdapter(activity_rideHistory.this,historyRecordArrayList);
                recyclerView.setAdapter(adapter);

            } else {
                Toast.makeText(activity_rideHistory.this, "date error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }



        public List<historyRecordEntity> performGetCall(String requestURL) {

            URL url;
            // ArrayList<historyRecordEntity> historyRecordEntityArrayList = new ArrayList<historyRecordEntity>();

            try {
                System.out.println("get history request: " + requestURL);
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

//                    InputStream inputStream = conn.getInputStream();
//
//                    if(inputStream == null){
//                        Toast.makeText(activity_rideHistory.this, "input stream is empty", Toast.LENGTH_SHORT).show();
//                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        JSONArray ja = new JSONArray(inputLine);

                        for(int i = 0; i < ja.length(); i++){
                            JSONObject jo = (JSONObject) ja.get(i);

                            String date = jo.getString("date");
                            String provider = jo.getString("provider");
                            String pickup = jo.getString("pickup");
                            String destination = jo.getString("destination");
                            String fee = jo.getString("fee");

                            historyRecordEntity historyRecordEntity = new historyRecordEntity(date,provider,pickup,destination,fee);
                            historyRecordArrayList.add(historyRecordEntity);
                        }
                    }
                } else {
                    Log.e(TAG, "14 - False - HTTP_OK");
                    historyRecordArrayList = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return historyRecordArrayList;
        }
    }

    /**
     * Represents an set some fake user history to test
     * the user.
     */
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

            //performPostCall(serverUrl, postParams);

            // TODO: submit the request here.
            return performPostCall(serverUrl, postParams).length() > 0;
            //return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mTestTask = null;

            if (success) {
                Toast.makeText(activity_rideHistory.this, "history updated", Toast.LENGTH_SHORT).show();
                //finish();

            } else {
                Toast.makeText(activity_rideHistory.this, "error message", Toast.LENGTH_SHORT).show();
                //register_email.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mTestTask = null;
        }



        public String performPostCall(String requestURL,
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

            /*
             * JSON
             */

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

    private void historyRecyclerView(){
        pageCounter  = (TextView) findViewById(R.id.pageCounter_history);
        nextPage = (Button) findViewById(R.id.nextButton_history);
        previousPage = (Button) findViewById(R.id.previousButten_history);
        historyRecordArrayList = new ArrayList<>();



        nextPage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                displayCard(page.getNextPage());
                pageCounter.setText("Page " + page.getPage());
                pageCounter.setTextSize(30);
            }


        });


        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayCard(page.getPrevPage());
                pageCounter.setText("Page " + page.getPage());
                pageCounter.setTextSize(30);

            }
        });
    }

    public void displayCard(int page){
        int pageNeedToLoad = page;
        recyclerView = (RecyclerView) findViewById(R.id.history_recyclerView);
        historyRecordArrayList = new ArrayList<>();

        load_data_from_server(pageNeedToLoad);

        //recyclerView.removeAllViews();
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomAdapter(this, historyRecordArrayList);
        recyclerView.setAdapter(adapter);


        ;
    }
    private void load_data_from_server(int pageNeedToLoad){
        mAuthTask = new UserSelectDateTask(userName, showStartTime,showEndTime,pageNeedToLoad,pageSize);
        mAuthTask.execute((Void) null);
    }
}


