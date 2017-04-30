package edu.ttu.spm.cheapride;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static edu.ttu.spm.cheapride.R.id.history_table;
import static edu.ttu.spm.cheapride.R.id.submit_history;
import static edu.ttu.spm.cheapride.R.id.wrap_content;
import static java.security.AccessController.getContext;

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
    TableRow tableRow;
    LinearLayout separator;
    TextView value;

    ArrayList<historyRecord> historyRecordArrayList;

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

    private void historyTable(){
        pageCounter  = (TextView) findViewById(R.id.pageCounter_history);
        nextPage = (Button) findViewById(R.id.nextButton_history);
        previousPage = (Button) findViewById(R.id.previousButten_history);

        nextPage.setOnClickListener(new View.OnClickListener() {
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
            historyRecordArrayList.add(new historyRecord(""+i+1,"02/15/2017","Uber","2606 31street 79410","33 University Ave.79210","$2"));
        }

        pageableArray = new Pageable<>(historyRecordArrayList);
        pageableArray.setPageSize(20);
        pageableArray.setPage(1);
        pageCounter.setText("Page " + pageableArray.getPage() + " of " + pageableArray.getMaxPages());
        displayPage();
    }

    public void displayPage(){
        TableLayout historyTable = (TableLayout) findViewById(history_table);
        addTitle();

        for (historyRecord v : pageableArray.getListForPage()) {
            //create a new row
            TableRow newRow = new TableRow(this);

            //create text view to add number
            TextView labelNUMBER = new TextView(this);
            labelNUMBER.setText(v.getNumber());
            labelNUMBER.setGravity(Gravity.CENTER);
            newRow.addView(labelNUMBER);

            //create text view to add date
            TextView labelDATE = new TextView(this);
            labelDATE.setText(String.valueOf(v.getDate()));
            labelDATE.setGravity(Gravity.CENTER);
            newRow.addView(labelDATE);

            //create text view to add provide
            TextView labelPROVIDE = new TextView(this);
            labelPROVIDE.setText(String.valueOf(v.getProvider()));
            labelPROVIDE.setGravity(Gravity.CENTER);
            newRow.addView(labelPROVIDE);

            //create text view to add pickup
            TextView labelPICKUP = new TextView(this);
            labelPICKUP.setText(String.valueOf(v.getPick()));
            labelPICKUP.setGravity(Gravity.CENTER);
            newRow.addView(labelPICKUP);

            //create text view to add destination
            TextView labelDESTINATION = new TextView(this);
            ;
            labelDESTINATION.setText(String.valueOf(v.getDate()));
            labelDESTINATION.setGravity(Gravity.CENTER);
            newRow.addView(labelDESTINATION);

            //create text view to add fee
            TextView labelFEE = new TextView(this);
            labelFEE.setText(String.valueOf(v.getFee()));
            labelFEE.setGravity(Gravity.CENTER);
            newRow.addView(labelFEE);

            //add new row into table
            historyTable.addView(newRow);

           // addSeparator();

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
        titleRow.addView(title0);

        //add title for date
        TextView title1 = new TextView(this);
        title1.setText(dateTitle);
        titleRow.addView(title1);

        //add title for provide
        TextView title2 = new TextView(this);
        title2.setText(providerTitle);
        titleRow.addView(title2);

        //add title for pickup
        TextView title3 = new TextView(this);
        title3.setText(pickupTitle);
        titleRow.addView(title3);

        //add title for destination
        TextView title4 = new TextView(this);
        title4.setText(destinationTitle);
        titleRow.addView(title4);

        //add title for fee
        TextView title5 = new TextView(this);
        title5.setText(feeTitle);
        titleRow.addView(title5);

        historyTable.addView(titleRow);

    }

//    private void addSeparator() {
//        Resources res = MainActivity.this.getResources();
//        separator = new LinearLayout(MainActivity.this);
//        separator.setOrientation(LinearLayout.VERTICAL);
//        separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
//        separator.setBackgroundColor(Color.parseColor("#5e7974"));
//        separator.setDividerDrawable(res.getDrawable(R.drawable.radius_middle));
//        maintable.addView(separator);
//
//    }
}



//        String numberTitle = "  No. ";
//        String dateTitle = "  Date  ";
//        String providerTitle = "   Provider   ";
//        String pickupTitle = "      Pickup      ";
//        String destinationTitle = "      Destination      ";
//        String feeTitle = "  Fee  ";
//
//        TableLayout historyTable = (TableLayout) findViewById(history_table);
//
//        //create row for title
//        TableRow titleRow = new TableRow(this);
//
//        //add title for number
//        TextView title0 = new TextView(this);
//        title0.setText(numberTitle);
//        titleRow.addView(title0);
//
//        //add title for date
//        TextView title1 = new TextView(this);
//        title1.setText(dateTitle);
//        titleRow.addView(title1);
//
//        //add title for provide
//        TextView title2 = new TextView(this);
//        title2.setText(providerTitle);
//        titleRow.addView(title2);
//
//        //add title for pickup
//        TextView title3 = new TextView(this);
//        title3.setText(pickupTitle);
//        titleRow.addView(title3);
//
//        //add title for destination
//        TextView title4 = new TextView(this);
//        title4.setText(destinationTitle);
//        titleRow.addView(title4);
//
//        //add title for fee
//        TextView title5 = new TextView(this);
//        title5.setText(feeTitle);
//        titleRow.addView(title5);
//
//        historyTable.addView(titleRow);
//
////        for(int i = 1; i <100; i++){
////
////            //int number = i ;
////            String date = "02/15/2017";
////            String provide = "Uber";
////            String pickup = "2606 31street 79410";
////            String destination = "33 University Ave.79210";
////            String fee = "$2";
////
////            //create a new row
////            TableRow newRow = new TableRow(this);
////
////            //create text view to add number
////            TextView labelNUMBER = new TextView(this);
////            labelNUMBER.setText("" + i);
////            labelNUMBER.setGravity(Gravity.CENTER);
////            newRow.addView(labelNUMBER);
////
////            //create text view to add date
////            TextView labelDATE  = new TextView(this);
////            labelDATE.setText(date);
////            labelDATE.setGravity(Gravity.CENTER);
////            newRow.addView(labelDATE);
////
////            //create text view to add provide
////            TextView labelPROVIDE = new TextView(this);
////            labelPROVIDE.setText(provide);
////            labelPROVIDE.setGravity(Gravity.CENTER);
////            newRow.addView(labelPROVIDE);
////
////            //create text view to add pickup
////            TextView labelPICKUP = new TextView(this);
////            labelPICKUP.setText(pickup);
////            labelPICKUP.setGravity(Gravity.CENTER);
////            newRow.addView(labelPICKUP);
////
////            //create text view to add destination
////            TextView labelDESTINATION = new TextView(this);;
////            labelDESTINATION.setText(destination);
////            labelDESTINATION.setGravity(Gravity.CENTER);
////            newRow.addView(labelDESTINATION);
////
////            //create text view to add fee
////            TextView labelFEE = new TextView(this);
////            labelFEE.setText(fee);
////            labelFEE.setGravity(Gravity.CENTER);
////            newRow.addView(labelFEE);
////
////            //add new row into table
////            historyTable.addView(newRow);
////        }
//    }
//}


