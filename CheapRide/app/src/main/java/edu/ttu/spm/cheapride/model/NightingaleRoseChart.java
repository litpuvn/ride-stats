package edu.ttu.spm.cheapride.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xclcharts.chart.PieData;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import edu.ttu.spm.cheapride.model.item.MyCustomPieData;
import edu.ttu.spm.cheapride.view.DemoView;

import static edu.ttu.spm.cheapride.model.item.Asset.MAX;
import static edu.ttu.spm.cheapride.model.item.Asset.MIN;
import static edu.ttu.spm.cheapride.model.item.Asset.MIN_SCALE;
import static edu.ttu.spm.cheapride.model.item.Asset.RANGE;
import static edu.ttu.spm.cheapride.model.item.Asset.VALUE;

/**
 * Created by Administrator on 2017/5/21.
 */

public class NightingaleRoseChart extends DemoView {

    private String TAG = "RadarChart03View";
    private CustomizedRoseChart chartRoseLyft = new CustomizedRoseChart();
    LinkedList<PieData> roseLyftData = new LinkedList<PieData>();
    private CustomizedRoseChart chartRoseUber = new CustomizedRoseChart();
    LinkedList<PieData> roseUberData = new LinkedList<PieData>();
    private List<String> labels = new LinkedList<String>();
    private double eastUber;
    private double eastLyft;
    private double westUber;
    private double westLyft;
    private double northUber;
    private double northLyft;
    private double southUber;
    private double southLyft;

    private double eastUberScaled;
    private double eastLyftScaled;
    private double westUberScaled;
    private double westLyftScaled;
    private double northUberScaled;
    private double northLyftScaled;
    private double southUberScaled;
    private double southLyftScaled;

    private int textSize = 30;
    private int x = -0;
    private int y = 10;
    private int padding = 100;
//    private String space = "         ";
    private String title = null;

    private static int ALPHA = 150;


    public NightingaleRoseChart(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView();
    }
//
//    public NightingaleRoseChart(Context context, Origin attrs){
//       super(context, attrs);
//        initView();
//    }

    public NightingaleRoseChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public NightingaleRoseChart(Context context,double eastUber,double eastLyft,double westUber,double westLyft,double northUber,double northLyft,double southUber,double southLyft,String title){
        super(context);

        this.title = title;

        this.eastUber = eastUber;
        this.eastUberScaled = scale(this.eastUber);

        this.eastLyft = eastLyft;
        this.eastLyftScaled = scale(this.eastLyft);

        this.westUber = westUber;
        this.westUberScaled = scale(this.westUber);

        this.westLyft = westLyft;
        this.westLyftScaled = scale(this.westLyft);

        this.northUber = northUber;
        this.northUberScaled = scale(this.northUber);

        this.northLyft = northLyft;
        this.northLyftScaled = scale(this.northLyft);

        this.southUber = southUber;
        this.southUberScaled = scale(this.southUber);

        this.southLyft = southLyft;
        this.southLyftScaled = scale(this.southLyft);

        initView();
    }

    public NightingaleRoseChart(Context context,double eastUber,double eastLyft,double westUber,double westLyft,double northUber,double northLyft,double southUber,double southLyft){
        super(context);

        this.eastUber = eastUber;
        this.eastUberScaled = scale(this.eastUber);

        this.eastLyft = eastLyft;
        this.eastLyftScaled = scale(this.eastLyft);

        this.westUber = westUber;
        this.westUberScaled = scale(this.westUber);

        this.westLyft = westLyft;
        this.westLyftScaled = scale(this.westLyft);

        this.northUber = northUber;
        this.northUberScaled = scale(this.northUber);

        this.northLyft = northLyft;
        this.northLyftScaled = scale(this.northLyft);

        this.southUber = southUber;
        this.southUberScaled = scale(this.southUber);

        this.southLyft = southLyft;
        this.southLyftScaled = scale(this.southLyft);

        initView();
    }

    private double scale(double price) {
        double scaledPrice;
        double temp;

        if(price >= MIN && price <MAX) {
            temp = ((MIN_SCALE +  RANGE * ((price)/VALUE))/10);
            scaledPrice = 10 * temp;
        }
        else {
            scaledPrice = 100;
        }

        return scaledPrice;
    }

//    public NightingaleRoseChart(Context context,Origin origin){
//        super(context);
//
//        eastUber = origin.getEast().getUber().getCost();
//        eastLyft = origin.getEast().getLyft().getCost();
//
//        westUber = origin.getWest().getUber().getCost();
//        westLyft = origin.getWest().getLyft().getCost();
//
//        northUber = origin.getNorth().getUber().getCost();
//        northLyft = origin.getNorth().getLyft().getCost();
//
//        southUber = origin.getSouth().getUber().getCost();
//        southLyft = origin.getSouth().getLyft().getCost();
//        initView();
//    }

    private void initView()
    {
        if(title != null) {
            chartRoseLyft.setTitle(title);
            chartRoseLyft.setTitleVerticalAlign(XEnum.VerticalAlign.MIDDLE);
        }
        //chartRoseLyft.addSubtitle("(Uber vs. Lyft)");
////
        chartDataSetLyft();
        chartLyftRender();

        chartDataSetUber();
        chartRenderUber();


        //綁定手势滑动事件
//        this.bindTouch(this,chartRoseLyft);
//        this.bindTouch(this,chartRoseUber);
//        this.bindTouch(this,chartRose1);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chartRoseLyft.setChartRange(w,h);
        chartRoseUber.setChartRange(w,h);
        //chartRose1.setChartRange(w,h);
    }

    private void chartDataSetUber()
    {
        //front rose chart
        //设置图表数据源
        //PieData(标签，百分比，在饼图中对应的颜色)
        PieData pie = new MyCustomPieData(formatNumberAsCurrency(eastUber),eastUberScaled,Color.argb(ALPHA, 20, 32, 43));
        roseUberData.add(pie);
        pie = new MyCustomPieData(formatNumberAsCurrency(westUber), westUberScaled, Color.argb(ALPHA, 20, 32, 43));
        roseUberData.add(pie);
        pie = new MyCustomPieData(formatNumberAsCurrency(southUber), southUberScaled, Color.argb(ALPHA, 20, 32, 43));
        roseUberData.add(pie);
        pie = new MyCustomPieData(formatNumberAsCurrency(northUber), northUberScaled, Color.argb(ALPHA, 20, 32, 43));
        roseUberData.add(pie);
    }

    private void chartLyftRender()
    {
        try {

            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            chartRoseLyft.setPadding(padding, padding, padding, padding);
            chartRoseLyft.setTranslateXY(x, y);

            //数据源
            chartRoseLyft.setDataSource(roseLyftData);

            chartRoseLyft.getInnerPaint().setStyle(Style.STROKE);
            chartRoseLyft.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            chartRoseLyft.getLabelPaint().setColor( Color.rgb(252,45, 166));
            chartRoseLyft.getLabelPaint().setTextSize(textSize);
            chartRoseLyft.setIntervalAngle(3);

            //--------------------------------------
            Map<Float,Integer>  mapBgSeg = new HashMap<Float,Integer>();
            mapBgSeg.put(0.8f, Color.rgb(39, 161, 237));
            mapBgSeg.put(0.6f, Color.rgb(246, 137, 31));
            chartRoseLyft.showBgCircle(mapBgSeg);

            chartRoseLyft.showBgLines(Color.BLUE);
            //chartRoseLyft.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);

            chartRoseLyft.showOuterLabels();

            chartRoseLyft.setLabelOffsetY(30);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    private void chartDataSetLyft()
    {
        //back rose chart
        //设置图表数据源

        //PieData(标签，百分比，在饼图中对应的颜色)

        roseLyftData.add(new MyCustomPieData(formatNumberAsCurrency(eastLyft),eastLyftScaled,Color.argb(ALPHA, 252,45, 166) ));
        roseLyftData.add(new MyCustomPieData(formatNumberAsCurrency(westLyft),westLyftScaled,Color.argb(ALPHA, 252,45, 166)));
        roseLyftData.add(new MyCustomPieData(formatNumberAsCurrency(southLyft),southLyftScaled,Color.argb(ALPHA, 252,45, 166)));
        roseLyftData.add(new MyCustomPieData(formatNumberAsCurrency(northLyft), northLyftScaled,Color.argb(ALPHA, 252,45, 166) ));
    }

    private void chartRenderUber()
    {
        try {

            //设置绘图区默认缩进px值
            //int [] ltrb = getPieDefaultSpadding();
            chartRoseUber.setPadding(padding, padding, padding, padding);
            chartRoseUber.setTranslateXY(x, y);
            //数据源
            chartRoseUber.setDataSource(roseUberData);
            chartRoseUber.getInnerPaint().setStyle(Style.STROKE);
            chartRoseUber.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            //chartRoseUber.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);

           // chartRoseUber.getLabelPaint().setColor(Color.parseColor("#D92222"));
            chartRoseUber.getLabelPaint().setColor( Color.rgb(20, 32, 43));
            chartRoseUber.getLabelPaint().setTextSize(textSize);

            chartRoseUber.setIntervalAngle(3);
            //chartRoseUber.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);

            chartRoseUber.showOuterLabels();

            chartRoseUber.setLabelOffsetY(-3);
//            chartRoseUber.setLabelOffsetX(5);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }



    @Override
    public void render(Canvas canvas) {
        try{
            chartRoseLyft.setBgLines(roseLyftData.size());
            chartRoseLyft.render(canvas);//label is here
            chartRoseUber.render(canvas);
//            chartRose1.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private String formatNumberAsCurrency(double val) {
        return formatNumberAsCurrency(val, true);
    }

    private String formatNumberAsCurrency(double val, boolean includeDollarSign) {
        return includeDollarSign ? "$" + String.format("%.1f", val) : String.format("%.1f", val) ;
    }
}
