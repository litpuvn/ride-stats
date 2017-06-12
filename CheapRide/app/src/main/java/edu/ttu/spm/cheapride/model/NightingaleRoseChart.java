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
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;

import edu.ttu.spm.cheapride.model.item.Origin;
import edu.ttu.spm.cheapride.view.DemoView;

import static org.xclcharts.renderer.XEnum.SliceLabelStyle.OUTSIDE;

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

    private int textSize = 30;

    private int x = -7;
    private int y = 10;

    private int padding = 100;

    private String space = "         ";

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
        northLyft = origin.getNorth().getLyft().getCost();

        southUber = origin.getSouth().getUber().getCost();
        southLyft = origin.getSouth().getLyft().getCost();
        initView();
    }

    private void initView()
    {
        //chartRose.setTitle("Comparison Chart");
        //chartRose.addSubtitle("(Uber vs. Lyft)");
//
        chartDataSet();
        chartRender();

        chartDataSet3();
        chartRender3();


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
        //chartRose1.setChartRange(w,h);
    }

    private void chartDataSet3()
    {
        //front rose chart
        //设置图表数据源
        //PieData(标签，百分比，在饼图中对应的颜色)
        if(eastUber <= eastLyft) {
            PieData pie = new PieData( "$" + String.valueOf(eastUber) + "/",eastUber,Color.rgb(20, 32, 43));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(20, 32, 43));
            roseData3.add(pie);
        }
        else {
            PieData pie = new PieData( "$" + String.valueOf(eastUber) + "/", eastLyft, Color.rgb(252, 45, 166));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(252, 45, 166));
            roseData3.add(pie);
        }

        if(southUber <= southLyft) {
            PieData pie = new PieData( "$" + String.valueOf(southUber) + "/", southUber, Color.rgb(20, 32, 43));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(20, 32, 43));
            roseData3.add(pie);
        }
        else {
            PieData pie = new PieData( "$" + String.valueOf(southUber) + "/", southLyft, Color.rgb(252, 45, 166));
            pie.setCustLabelStyle(OUTSIDE,Color.rgb(252, 45, 166));
            roseData3.add(pie);
        }

        if(westUber <= westLyft) {
            PieData pie = new PieData( "$" + String.valueOf(westUber) + "/" + space, westUber, Color.rgb(20, 32, 43));
            pie.setCustLabelStyle(OUTSIDE,Color.rgb(20, 32, 43));
            roseData3.add(pie);
        }
        else {
            PieData pie = new PieData( "$" + String.valueOf(westUber) + "/" + space, westLyft, Color.rgb(252, 45, 166));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(252, 45, 166));
            roseData3.add(pie);
        }

        if(northUber <= northLyft) {
            PieData pie = new PieData( "$" + String.valueOf(northUber) + "/", northUber, Color.rgb(20, 32, 43));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(20, 32, 43));
            roseData3.add(pie);
        }
        else {
            PieData pie = new PieData( "$" + String.valueOf(northUber) + "/", northLyft, Color.rgb(252, 45, 166));
            //pie.setCustLabelStyle(OUTSIDE,Color.rgb(252, 45, 166));
            roseData3.add(pie);
        }
    }

    private void chartRender()
    {
        try {

            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            chartRose.setPadding(padding, padding, padding, padding);
            chartRose.setTranslateXY(x, y);
            //数据源
            chartRose.setDataSource(roseData);

            chartRose.getInnerPaint().setStyle(Style.STROKE);
            chartRose.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            chartRose.getLabelPaint().setColor( Color.rgb(252,45, 166));
            chartRose.getLabelPaint().setTextSize(textSize);
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


    private void chartDataSet()
    {
        //back rose chart
        //设置图表数据源

        //PieData(标签，百分比，在饼图中对应的颜色)

        if(eastUber > eastLyft)
            roseData.add(new PieData("            " + eastLyft,eastUber,Color.rgb(20, 32, 43) ));
        else
            roseData.add(new PieData("            " + eastLyft,eastLyft,Color.rgb(252,45, 166) ));

        if(southUber > southLyft)
            roseData.add(new PieData("            " + southLyft,southUber,Color.rgb(20, 32, 43)));
        else
            roseData.add(new PieData("            " + southLyft,southLyft,Color.rgb(252,45, 166)));

        if(westUber > westLyft)
            roseData.add(new PieData("            " + westLyft + space,westUber,Color.rgb(20, 32, 43)));
        else
            roseData.add(new PieData("            " + westLyft + space,westLyft,Color.rgb(252,45, 166)));

        if(northUber > northLyft)
            roseData.add(new PieData("            " + northLyft,northUber,Color.rgb(20, 32, 43) ));
        else
            roseData.add(new PieData("            " + northLyft,northLyft,Color.rgb(252,45, 166) ));
    }

    private void chartRender3()
    {
        try {

            //设置绘图区默认缩进px值
            //int [] ltrb = getPieDefaultSpadding();
            chartRose3.setPadding(padding, padding, padding, padding);
            chartRose3.setTranslateXY(x, y);
            //数据源
            chartRose3.setDataSource(roseData3);
            chartRose3.getInnerPaint().setStyle(Style.STROKE);
            chartRose3.setInitialAngle(315 - 1);

            //设置标签显示位置,当前设置标签显示在扇区中间
            //chartRose3.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);

           // chartRose3.getLabelPaint().setColor(Color.parseColor("#D92222"));
            chartRose3.getLabelPaint().setColor( Color.rgb(20, 32, 43));
            chartRose3.getLabelPaint().setTextSize(textSize);

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
            chartRose.render(canvas);//label is here
            chartRose3.render(canvas);
//            chartRose1.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
