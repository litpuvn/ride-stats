package edu.ttu.spm.cheapride;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PopupActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        
        getWindow().setLayout((int)(width * 0.8),(int)(height * 0.6));
    }
}
