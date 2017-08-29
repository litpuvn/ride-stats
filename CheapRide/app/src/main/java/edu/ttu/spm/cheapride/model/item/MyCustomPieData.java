package edu.ttu.spm.cheapride.model.item;

import org.xclcharts.chart.PieData;
import org.xclcharts.renderer.XEnum;

/**
 * Created by hoanglong on 16/06/2017.
 */

public class MyCustomPieData extends PieData {

    private float offSetX;
    private float offSetY;

    public MyCustomPieData() {
       super();
    }

    public MyCustomPieData(String label, double percent, int color) {
       super(label, percent, color);
    }

    public MyCustomPieData(String label, double percent, int color, boolean selected) {
        super(label, percent, color, selected);

    }

    public MyCustomPieData(String key, String label, double percent, int color) {
        super(key, label, percent, color);

    }

    public MyCustomPieData(String key, String label, double percent, int color, boolean selected) {
        super(key, label, percent, color, selected);

    }

    public float getOffSetX() {
        return offSetX;
    }

    public void setOffSetX(float offSetX) {
        this.offSetX = offSetX;
    }

    public float getOffSetY() {
        return offSetY;
    }

    public void setOffSetY(float offSetY) {
        this.offSetY = offSetY;
    }
}
