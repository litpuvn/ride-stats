package edu.ttu.spm.cheapride;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static edu.ttu.spm.cheapride.R.id.history_table;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        //show the date picker
        showDialogOnTextViewClick();

        //insert new rows
        insertRow();
    }

    public void showDialogOnTextViewClick(){
        startDate = (EditText)findViewById(R.id.startDate_history);
        endDate = (EditText)findViewById(R.id.endDate_history);

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
            =new DatePickerDialog.OnDateSetListener() {
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

    private void insertRow(){
        String numberTitle = "  No. ";
        String dateTitle = "  Date  ";
        String provideTitle = "   Provider   ";
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
        title2.setText(provideTitle);
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

        for(int i = 1; i <100; i++){

            //int number = i ;
            String date = "02/15/2017";
            String provide = "Uber";
            String pickup = "2606 31street 79410";
            String destination = "33 University Ave.79210";
            String fee = "$2";

            //create a new row
            TableRow newRow = new TableRow(this);

            //create text view to add number
            TextView labelNUMBER = new TextView(this);
            labelNUMBER.setText("" + i);
            labelNUMBER.setGravity(Gravity.CENTER);
            newRow.addView(labelNUMBER);

            //create text view to add date
            TextView labelDATE  = new TextView(this);
            labelDATE.setText(date);
            labelDATE.setGravity(Gravity.CENTER);
            newRow.addView(labelDATE);

            //create text view to add provide
            TextView labelPROVIDE = new TextView(this);
            labelPROVIDE.setText(provide);
            labelPROVIDE.setGravity(Gravity.CENTER);
            newRow.addView(labelPROVIDE);

            //create text view to add pickup
            TextView labelPICKUP = new TextView(this);
            labelPICKUP.setText(pickup);
            labelPICKUP.setGravity(Gravity.CENTER);
            newRow.addView(labelPICKUP);

            //create text view to add destination
            TextView labelDESTINATION = new TextView(this);;
            labelDESTINATION.setText(destination);
            labelDESTINATION.setGravity(Gravity.CENTER);
            newRow.addView(labelDESTINATION);

            //create text view to add fee
            TextView labelFEE = new TextView(this);
            labelFEE.setText(fee);
            labelFEE.setGravity(Gravity.CENTER);
            newRow.addView(labelFEE);

            //add new row into table
            historyTable.addView(newRow);
        }
    }
}


