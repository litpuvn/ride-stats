package edu.ttu.spm.cheapride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class activity_rideHistory extends AppCompatActivity {

    private TableLayout historyTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        historyTable = (TableLayout) findViewById(R.id.history_table);
        //insert new rows
        insertRow();
    }

    private void insertRow(){
        for(int i = 0; i <4; i++){
            int number = i + 1;
            String date = "02/15/2017";
            String provide = "Uber";
            String pickup = "2606 31street 79410";
            String destination = "33 University Ave.79210";
            String fee = "$2";

            TableRow newRow = new TableRow(this);
            TableRow.LayoutParams attrs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            newRow.setLayoutParams(attrs);

            TextView labelNUMBER = new TextView(this);
            labelNUMBER.setId(i*10+1);
            labelNUMBER
        }
    }
}
