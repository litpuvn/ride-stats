package edu.ttu.spm.cheapride;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static edu.ttu.spm.cheapride.R.id.history_table;
import static edu.ttu.spm.cheapride.R.id.wrap_content;
import static java.security.AccessController.getContext;

public class activity_rideHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

       //insert new rows
        insertRow();
    }

    private void insertRow(){
        String numberTitle = "  No. ";
        String dateTitle = "  Date  ";
        String provideTitle = "   Provide   ";
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


