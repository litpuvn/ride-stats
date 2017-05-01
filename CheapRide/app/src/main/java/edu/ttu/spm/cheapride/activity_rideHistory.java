package edu.ttu.spm.cheapride;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static edu.ttu.spm.cheapride.R.id.history_table;

public class activity_rideHistory extends AppCompatActivity {

    private EditText startDate;
    private EditText endDate;
    int year_start;
    int month_start;
    int day_start;
    int year_end;
    int month_end;
    int day_end;
    int DIALOG_ID = 0;
    private Button date_submit;
    Pageable<historyRecord> pageableArray;
    TextView pageCounter;
    Button nextPage;
    Button previousPage;
    LinearLayout separator;
    ArrayList<historyRecord> historyRecordArrayList;

    private activity_rideHistory.UserSelectDateTask mAuthTask = null;
    private activity_rideHistory.SetHistoryDateTask mTestTask = null;
    private static final int READ_TIMEOUT = 30000; // seconds
    private static final int CONNECTION_TIMEOUT = 30000; // seconds
    private final String TAG = "post json example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        //set default date
        setDefaultDate();

        //show the date picker
        showDialogOnTextViewClick();

        //insert new rows
        historyTable();

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
                for(int i = 0; i <= 100; i++) {
                    //TODO Auto-generated method stub
                    String username = "john";
                    String date = "09/09/2017";
                    String pick = "broadway avenue";
                    String destination = "University avenue";
                    String fee = "$20";
                    String provider = "uber";

                    mTestTask = new SetHistoryDateTask(username, date, pick, destination, fee, provider);
                    mTestTask.execute((Void) null);
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
                String showStartTime = month_start + "/" + day_start + "/" + year_start;

                //show date on the text view
                startDate.setText(showStartTime);
                return;
            }
            if(DIALOG_ID ==2){
                year_end = year;
                month_end = month;
                day_end = day;
                String showEndTime = month_end + "/" + day_end + "/" + year_end;

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

    //create history record class
    public class historyRecord{
        private String number;
        private String date;
        private String provider;
        private String pick;
        private String destination;
        private String fee;

        public historyRecord(String number,String date,String provider,String pick,String destination,String fee){
            this.number = number;
            this.date = date;
            this.provider = provider;
            this.pick = pick;
            this.destination = destination;
            this.fee = fee;
        }
        public String getNumber(){return number;}
        public String getDate(){return date;}
        public String getProvider(){return provider;}
        public String getPick(){return pick;}
        public String getDestination(){return destination;}
        public String getFee(){return fee;}
    }

    /**
     * Represents an asynchronous user history task used to authenticate
     * the user.
     */
    public class UserSelectDateTask extends AsyncTask<Void, Void, Boolean> {

        private  String mUserName;
        private  String mStartDate;
        private  String mEndDate;


        UserSelectDateTask(String userName, String startDate, String endDate) {
            mUserName = userName;
            mStartDate = startDate;
            mEndDate = endDate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = MainActivity.BASE_URL + "/history";
            HashMap<String, String> postParams = new HashMap<>();

            postParams.put("username", mUserName);
            postParams.put("from_date", mStartDate);
            postParams.put("to_date",mEndDate);

            //performPostCall(serverUrl, postParams);

            // TODO: submit the request here.
            return performPostCall(serverUrl, postParams).length() > 0;
            //return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Toast.makeText(activity_rideHistory.this, "loading", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(activity_rideHistory.this, "date error", Toast.LENGTH_SHORT).show();
                //register_email.requestFocus();
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
                root.put("from_date", postDataParams.get("from_date"));
                root.put("to_date", postDataParams.get("to_date"));

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
                finish();

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

    private void historyTable(){
        pageCounter  = (TextView) findViewById(R.id.pageCounter_history);
        nextPage = (Button) findViewById(R.id.nextButton_history);
        previousPage = (Button) findViewById(R.id.previousButten_history);
        historyRecordArrayList = new ArrayList<>();

        nextPage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                pageableArray.setPage(pageableArray.getNextPage());
//                tableRow.removeAllViews();
                displayPage();
                pageCounter.setText("Page " + pageableArray.getPage() + " of " + pageableArray.getMaxPages());
            }


        });


        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageableArray.setPage(pageableArray.getPreviousPage());
//                tableRow.removeAllViews();
                displayPage();
                pageCounter.setText("Page " + pageableArray.getPage() + " of " + pageableArray.getMaxPages());

            }
        });

        for(int i = 0; i <= 100; i++){
            //here to input the content of list
            historyRecordArrayList.add(new historyRecord(" "+i,"02/15/2017","Uber","2606 31street 79410","33 University Ave.79210","$2"));
        }

        pageableArray = new Pageable<>(historyRecordArrayList);
        pageableArray.setPageSize(13);
        pageableArray.setPage(1);
        pageCounter.setText("Page " + pageableArray.getPage() + " of " + pageableArray.getMaxPages());
        pageCounter.setTextColor(Color.BLACK);
        pageCounter.setTextSize(30);
        displayPage();
    }

    public void displayPage(){
        TableLayout historyTable = (TableLayout) findViewById(history_table);

        historyTable.removeAllViews();
        addTitle();

        for (historyRecord v : pageableArray.getListForPage()) {
            //create a new row
            TableRow newRow = new TableRow(this);

            //create text view to add number
            TextView labelNUMBER = new TextView(this);
            labelNUMBER.setText(v.getNumber());
            labelNUMBER.setGravity(Gravity.CENTER);
            labelNUMBER.setTextColor(Color.MAGENTA);
            labelNUMBER.setTextSize(25);
            newRow.addView(labelNUMBER);

            //create text view to add date
            TextView labelDATE = new TextView(this);
            labelDATE.setText(String.valueOf(v.getDate()));
            labelDATE.setGravity(Gravity.CENTER);
            labelDATE.setTextColor(Color. BLUE);
            labelDATE.setTextSize(25);
            newRow.addView(labelDATE);

            //create text view to add provide
            TextView labelPROVIDE = new TextView(this);
            labelPROVIDE.setText(String.valueOf(v.getProvider()));
            labelPROVIDE.setGravity(Gravity.CENTER);
            labelPROVIDE.setTextColor(Color.MAGENTA);
            labelPROVIDE.setTextSize(25);
            newRow.addView(labelPROVIDE);

            //create text view to add pickup
            TextView labelPICKUP = new TextView(this);
            labelPICKUP.setText(String.valueOf(v.getPick()));
            labelPICKUP.setGravity(Gravity.CENTER);
            labelPICKUP.setTextColor(Color. BLUE);
            labelPICKUP.setTextSize(25);
            newRow.addView(labelPICKUP);

            //create text view to add destination
            TextView labelDESTINATION = new TextView(this);
            labelDESTINATION.setText(String.valueOf(v.getDestination()));
            labelDESTINATION.setGravity(Gravity.CENTER);
            labelDESTINATION.setTextColor(Color.MAGENTA);
            labelDESTINATION.setTextSize(25);
            newRow.addView(labelDESTINATION);

            //create text view to add fee
            TextView labelFEE = new TextView(this);
            labelFEE.setText(String.valueOf(v.getFee()));
            labelFEE.setGravity(Gravity.CENTER);
            labelFEE.setTextColor(Color. BLUE);
            labelFEE.setTextSize(25);
            newRow.addView(labelFEE);

            //add new row into table
            historyTable.addView(newRow);

           addSeparator();

        }
    }

    public void addTitle(){
        String numberTitle = "  No. ";
        String dateTitle = "  Date  ";
        String providerTitle = "   Provider   ";
        String pickupTitle = "      Pickup      ";
        String destinationTitle = "      Destination      ";
        String feeTitle = "  Fee  ";

        TableLayout historyTable = (TableLayout) findViewById(history_table);

        //create row for title
        TableRow titleRow = new TableRow(this);

        //add title for number
        TextView title0 = new TextView(this);
        title0.setText(numberTitle);
        title0.setTextColor(Color.BLACK);
        title0.setTextSize(30);
        titleRow.addView(title0);


        //add title for date
        TextView title1 = new TextView(this);
        title1.setText(dateTitle);
        title1.setTextColor(Color.BLACK);
        title1.setTextSize(30);
        titleRow.addView(title1);

        //add title for provide
        TextView title2 = new TextView(this);
        title2.setText(providerTitle);
        title2.setTextColor(Color.BLACK);
        title2.setTextSize(30);
        titleRow.addView(title2);

        //add title for pickup
        TextView title3 = new TextView(this);
        title3.setText(pickupTitle);
        title3.setTextColor(Color.BLACK);
        title3.setTextSize(30);
        titleRow.addView(title3);

        //add title for destination
        TextView title4 = new TextView(this);
        title4.setText(destinationTitle);
        title4.setTextColor(Color.BLACK);
        title4.setTextSize(30);
        titleRow.addView(title4);

        //add title for fee
        TextView title5 = new TextView(this);
        title5.setText(feeTitle);
        title5.setTextColor(Color.BLACK);
        title5.setTextSize(30);
        titleRow.addView(title5);

        historyTable.addView(titleRow);

    }

    private void addSeparator() {
        TableLayout historyTable = (TableLayout) findViewById(history_table);

        Resources res = activity_rideHistory.this.getResources();
        separator = new LinearLayout(activity_rideHistory.this);
        separator.setOrientation(LinearLayout.VERTICAL);
        separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
        separator.setBackgroundColor(Color.parseColor("#5e7974"));
        //separator.setDividerDrawable(res.getDrawable(R.drawable.radius_middle));
        historyTable.addView(separator);

    }


}


