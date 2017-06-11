package edu.ttu.spm.cheapride.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xclcharts.chart.PieData;
import org.xclcharts.chart.RoseChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import edu.ttu.spm.cheapride.MainActivity;
import edu.ttu.spm.cheapride.model.item.Origin;
import edu.ttu.spm.cheapride.view.DemoView;

/**
 * Created by Administrator on 2017/5/21.
 */

public class NightingaleRoseChart extends DemoView {

    private String TAG = "RadarChart03View";
    private RoseChart chartRose = new RoseChart();
    LinkedList<PieData> roseData = new LinkedList<PieData>();

    private RoseChart chartRose3 = new RoseChart();
    LinkedList<PieData> roseData3 = new LinkedList<PieData>();

    private RoseChart chartRose1 = new RoseChart();
    LinkedList<PieData> roseData1 = new LinkedList<PieData>();

    //标签集合
    private List<String> labels = new LinkedList<String>();

    //private RadarChart chart = new RadarChart();
//	private List<RadarData> chartData = new LinkedList<RadarData>();

    private int eastUber;
    private int eastLyft;

    private int westUber;
    private int westLyft;

    private int northUber;
    private int northLyft;

    private int southUber;
    private int southLyft;

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

    public NightingaleRoseChart(Context context,Origin origin){
        super(context);

        eastUber = origin.getEast().getUber().getCost();
        eastLyft = origin.getEast().getLyft().getCost();

        westUber = origin.getWest().getUber().getCost();
        westLyft = origin.getWest().getLyft().getCost();

        northUber = origin.getNorth().getUber().getCost();
        northUber = origin.getNorth().getLyft().getCost();

        southUber = origin.getSouth().getUber().getCost();
        southLyft = origin.getSouth().getLyft().getCost();

        initView();
    }

    private void initView()
    {
        //chartRose.setTitle("Comparison Chart");
        //chartRose.addSubtitle("(Uber vs. Lyft)");

        chartLabels();

        //chartRender1();
        //chartDataSet1();

        chartRender2();
        chartDataSet2();

        chartRender3();
        chartDataSet3();

        //綁定手势滑动事件
//        this.bindTouch(this,chartRose);
//        this.bindTouch(this,chartRose3);
//        this.bindTouch(this,chartRose1);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chartRose.setChartRange(w,h);
        chartRose3.setChartRange(w,h);
        chartRose1.setChartRange(w,h);
    }

    private void chartLabels()
    {
        labels.add("A1");
        labels.add("A2");
        labels.add("A3");
        labels.add("A4");

    }
    private void chartDataSet2()
    {
        //front rose chart
        //设置图表数据源

        //PieData(标签，百分比，在饼图中对应的颜色)
        if(eastUber <= eastLyft)
            roseData3.add(new PieData("E",eastUber,Color.rgb(20, 32, 43) ));
        else
            roseData3.add(new PieData("E",eastLyft,Color.rgb(252,45, 166) ));

        if(southUber <= southLyft)
            roseData3.add(new PieData("S",southUber,Color.rgb(20, 32, 43)));
        else
            roseData3.add(new PieData("S",southLyft,Color.rgb(252,45, 166)));

        if(westUber <= westLyft)
            roseData3.add(new PieData("W",westUber,Color.rgb(20, 32, 43)));
        else
            roseData3.add(new PieData("W",westLyft,Color.rgb(252,45, 166)));

        if(northUber <= northLyft)
            roseData3.add(new PieData("N",northUber,Color.rgb(20, 32, 43) ));
        else
            roseData3.add(new PieData("N",northLyft,Color.rgb(252,45, 166) ));
    }

    private void chartRender2()
    {
        try {

            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            chartRose.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            //数据源
            chartRose.setDataSource(roseData);
            chartRose.getInnerPaint().setStyle(Style.STROKE);
            chartRose.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            //chartRose.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);

            chartRose.setIntervalAngle(3);

            //--------------------------------------
            Map<Float,Integer>  mapBgSeg = new HashMap<Float,Integer>();
            mapBgSeg.put(0.8f, Color.rgb(39, 161, 237));
            mapBgSeg.put(0.6f, Color.rgb(246, 137, 31));
            chartRose.showBgCircle(mapBgSeg);

            chartRose.showBgLines(Color.BLUE);
            //chartRose.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);

            chartRose.showOuterLabels();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }


    private void chartDataSet3()
    {
        //back rose chart
        //设置图表数据源

        //PieData(标签，百分比，在饼图中对应的颜色)

//        roseData.add(new PieData("",eastLyft,Color.rgb(149, 206, 255)));  //(int)Color.rgb(39, 51, 72)));
//        roseData.add(new PieData("",southLyft,Color.rgb(149, 206, 255)));  //(int)Color.rgb(39, 51, 72)));
//        roseData.add(new PieData("",westLyft,Color.rgb(149, 206, 255)));  //(int)Color.rgb(39, 51, 72)));
//        roseData.add(new PieData("",northUber,Color.rgb(149, 206, 255)));  //(int)Color.rgb(77, 83, 97) ));

        if(eastUber > eastLyft)
            roseData.add(new PieData("",eastUber,Color.rgb(20, 32, 43) ));
        else
            roseData.add(new PieData("",eastLyft,Color.rgb(252,45, 166) ));

        if(southUber > southLyft)
            roseData.add(new PieData("",southUber,Color.rgb(20, 32, 43)));
        else
            roseData.add(new PieData("",southLyft,Color.rgb(252,45, 166)));

        if(westUber > westLyft)
            roseData.add(new PieData("",westUber,Color.rgb(20, 32, 43)));
        else
            roseData.add(new PieData("",westLyft,Color.rgb(252,45, 166)));

        if(northUber > northLyft)
            roseData.add(new PieData("",northUber,Color.rgb(20, 32, 43) ));
        else
            roseData.add(new PieData("",northLyft,Color.rgb(252,45, 166) ));
    }

    private void chartRender3()
    {
        try {

            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            chartRose3.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            //数据源
            chartRose3.setDataSource(roseData3);
            chartRose3.getInnerPaint().setStyle(Style.STROKE);
            chartRose3.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            //chartRose3.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);

            chartRose3.getLabelPaint().setColor(Color.parseColor("#D92222"));

            chartRose3.setIntervalAngle(3);
            //chartRose3.setLabelStyle(XEnum.SliceLabelStyle.OUTSIDE);

            chartRose3.showOuterLabels();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }



    @Override
    public void render(Canvas canvas) {
        try{
            chartRose.setBgLines(roseData.size());
            chartRose.render(canvas);
            chartRose3.render(canvas);
            chartRose1.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
