package edu.ttu.spm.cheapride.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import org.xclcharts.chart.PieData;
import org.xclcharts.chart.RoseChart;
import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.renderer.XEnum;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.ttu.spm.cheapride.model.item.MyCustomPieData;

/**
 * Created by hoanglong on 16/06/2017.
 */

public class CustomizedRoseChart extends RoseChart {
    private static final String TAG = "PieChart";
    private Paint mPaintInner = null;
    private boolean mShowInner = true;
    private int mIntervalAngle = 0;
    private Paint mPaintBg = null;
    private boolean mShowBgLines = false;
    private boolean mShowBgCircle = false;
    private Map<Float, Integer> mListBgSeg = null;
    private int mShowBgLineColor = -16777216;
    private boolean mShowOuterLabels = false;
    private int mBgLines = 0;
    private float labelOffSetX = 0;
    private float labelOffSetY = 0;

    public CustomizedRoseChart() {
        super();
    }

    public XEnum.ChartType getType() {
        return XEnum.ChartType.ROSE;
    }

    private void initChart() {
        if(this.getLabelPaint() != null) {
            this.getLabelPaint().setColor(-1);
            this.getLabelPaint().setTextSize(22.0F);
            this.getLabelPaint().setTextAlign(Paint.Align.CENTER);
        }

    }

    public Paint getInnerPaint() {
        if(this.mPaintInner == null) {
            this.mPaintInner = new Paint();
            this.mPaintInner.setColor(Color.rgb(68, 68, 68));
            this.mPaintInner.setStyle(Paint.Style.FILL);
            this.mPaintInner.setAntiAlias(true);
        }

        return this.mPaintInner;
    }

    public void setIntervalAngle(int angle) {
        this.mIntervalAngle = angle;
    }

    public void showInner() {
        this.mShowInner = true;
    }

    public void hideInner() {
        this.mShowInner = false;
    }

    public void showOuterLabels() {
        this.mShowOuterLabels = true;
    }

    public void hideOuterLabels() {
        this.mShowOuterLabels = false;
    }

    public Paint getBgPaint() {
        if(this.mPaintBg == null) {
            this.mPaintBg = new Paint(1);
            this.mPaintBg.setStyle(Paint.Style.STROKE);
            this.mPaintBg.setAntiAlias(true);
        }

        return this.mPaintBg;
    }

    public void showBgLines(int color) {
        this.mShowBgLines = true;
        this.mShowBgLineColor = color;
    }

    public void showBgCircle(Map<Float, Integer> bgSeg) {
        this.mShowBgCircle = true;
        this.mListBgSeg = bgSeg;
    }

    public void hideBgLines() {
        this.mShowBgLines = false;
    }

    public void hideBgCircle() {
        this.mShowBgCircle = true;
    }

    public void setBgLines(int count) {
        this.mBgLines = count;
    }

    protected boolean validateParams() {
        return true;
    }

    private void drawBGCircle(Canvas canvas) {
        if(this.mShowBgCircle) {
            if(this.mListBgSeg != null) {
                float radius = this.getRadius();
                Iterator var4 = this.mListBgSeg.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry entry = (Map.Entry)var4.next();
                    float newRadius = this.mul(radius, ((Float)entry.getKey()).floatValue());
                    if(Float.compare(newRadius, 0.0F) != 0 && Float.compare(newRadius, 0.0F) != -1) {
                        this.getBgPaint().setColor(((Integer)entry.getValue()).intValue());
                        canvas.drawCircle(this.plotArea.getCenterX(), this.plotArea.getCenterY(), newRadius, this.getBgPaint());
                    }
                }

            }
        }
    }

    private void drawBGLines(Canvas canvas) {
        if(this.mShowBgLines) {
            if(this.mBgLines != 0) {
                int totalAngle = 360 - this.mIntervalAngle * this.mBgLines;
                float currAngle = (float)(totalAngle / this.mBgLines);
                float radius = this.getRadius();
                float offsetAngle = this.mOffsetAngle;

                for(int i = 0; i < this.mBgLines; ++i) {
                    PointF pointbg = MathHelper.getInstance().calcArcEndPointXY(this.plotArea.getCenterX(), this.plotArea.getCenterY(), radius, offsetAngle + (float)this.mIntervalAngle + currAngle / 2.0F);
                    this.getBgPaint().setColor(mShowBgLineColor);
                    canvas.drawLine(this.plotArea.getCenterX(), this.plotArea.getCenterY(), pointbg.x, pointbg.y, this.getBgPaint());
                    offsetAngle = this.add(this.add(offsetAngle, currAngle), (float)this.mIntervalAngle);
                }

            }
        }
    }

    private float getLabelRadius() {
        float labelRadius = 0.0F;
        float radius = this.getRadius();
        if(this.mShowOuterLabels) {
            labelRadius = radius + DrawHelper.getInstance().getPaintFontHeight(this.getLabelPaint());
        } else {
            labelRadius = radius - radius / 2.0F / 2.0F;
        }

        return labelRadius;
    }

    protected boolean renderPlot(Canvas canvas) {
        float cirX = this.plotArea.getCenterX();
        float cirY = this.plotArea.getCenterY();
        float radius = this.getRadius();
        float arcAngle = 0.0F;
        float newRaidus = 0.0F;
        List chartDataSource = this.getDataSource();
        if(chartDataSource != null && chartDataSource.size() != 0) {
            if(this.mShowInner) {
                canvas.drawCircle(cirX, cirY, radius, this.getInnerPaint());
            }

            this.drawBGCircle(canvas);
            this.drawBGLines(canvas);
            int totalAngle = 360 - this.mIntervalAngle * chartDataSource.size();
            arcAngle = (float)(totalAngle / chartDataSource.size());
            arcAngle = this.div(this.mul(arcAngle, 100.0F), 100.0F);
            if(!this.validateAngle(arcAngle)) {
                Log.e("PieChart", "计算出来的扇区角度小于等于0度,不能绘制.");
                return false;
            } else {
                float labelRadius = this.getLabelRadius();

                for(Iterator var11 = chartDataSource.iterator(); var11.hasNext(); this.mOffsetAngle = this.add(this.add(this.mOffsetAngle, arcAngle), (float)this.mIntervalAngle)) {
                    PieData cData = (PieData)var11.next();
                    this.geArcPaint().setColor(cData.getSliceColor());
                    double p = cData.getPercentage() / 100.0D;
                    newRaidus = this.mul(radius, (float)p);
                    newRaidus = this.div(this.mul(newRaidus, 100.0F), 100.0F);
                    RectF nRF = new RectF(this.sub(cirX, newRaidus), this.sub(cirY, newRaidus), this.add(cirX, newRaidus), this.add(cirY, newRaidus));
                    canvas.drawArc(nRF, this.mOffsetAngle + (float)this.mIntervalAngle, arcAngle, true, this.geArcPaint());
                    String label = cData.getLabel();
                    if("" != label) {
                        float pOffsetX = 0;
                        float pOffsetY = 0;
                        if (cData instanceof MyCustomPieData) {
                            pOffsetX = ((MyCustomPieData) cData).getOffSetX();
                            pOffsetY = ((MyCustomPieData) cData).getOffSetY();
                        }
                        PointF point = MathHelper.getInstance().calcArcEndPointXY(cirX, cirY, labelRadius, this.mOffsetAngle + (float)this.mIntervalAngle + arcAngle / 2.0F);
                        DrawHelper.getInstance().drawRotateText(label, point.x + this.labelOffSetX + pOffsetX, point.y + this.labelOffSetY + pOffsetY, cData.getItemLabelRotateAngle(), canvas, this.getLabelPaint());
                    }
                }

                return true;
            }
        } else {
            Log.e("PieChart", "数据源为空.");
            return false;
        }
    }

    public void setLabelOffsetX(float x) {
        this.labelOffSetX = x;
    }

    public void setLabelOffsetY(float y) {
        this.labelOffSetY = y;
    }
}
