package edu.ttu.spm.cheapride;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xclcharts.chart.RoseChart;

import edu.ttu.spm.cheapride.model.NightingaleRoseChart;
import edu.ttu.spm.cheapride.model.item.Origin;
import edu.ttu.spm.cheapride.view.DemoView;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PopupActivity extends AppCompatActivity {

    private DemoView mCharts;
    private LinearLayout RoseChart;

    private Origin fakeOrigin1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        
        getWindow().setLayout((int)(width * 0.8),(int)(height * 0.6));

        initRoseChart();
    }

    public void initRoseChart()
    {

        fakeOrigin1 = Origin.createMe("Lubbock");
        //图表的使用方法:
        //使用方式一:
        // 1.新增一个Activity
        // 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
        // 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
        // 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

        //使用方式二:
        //代码调用 方式有下面二种方法:
        //方法一:
        //在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
        // 1. 新增一个View，绘制图表.
        // 2. 通过下面的代码得到控件，addview即可
        //LayoutInflater factory = LayoutInflater.from(this);
        //View content = (View) factory.inflate(R.layout.activity_multi_touch, null);


        //方法二:
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM|Gravity.RIGHT;

		   /*
		  //缩放控件放置在FrameLayout的上层，用于放大缩小图表
	       mZoomControls = new ZoomControls(this);
	       mZoomControls.setIsZoomInEnabled(true);
	       mZoomControls.setIsZoomOutEnabled(true);
		   mZoomControls.setLayoutParams(frameParm);
		   */

        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.5);
        int scrHeight = (int) (dm.heightPixels * 0.5);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        //final RelativeLayout chartLayout = new RelativeLayout(this);
        RoseChart = (LinearLayout) findViewById(R.id.rose_chart);

        mCharts = new NightingaleRoseChart(this,fakeOrigin1);

        RoseChart.addView( mCharts, layoutParams);

        //增加控件
        //((ViewGroup) content).addView(RoseChart);
        //((ViewGroup) content).addView(mZoomControls);
        //NightingaleRoseChart mCharts = new NightingaleRoseChart(this);setContentView(content);
        //放大监听
        //  mZoomControls.setOnZoomInClickListener(new OnZoomInClickListenerImpl());
        //缩小监听
        //  mZoomControls.setOnZoomOutClickListener(new OnZoomOutClickListenerImpl());
    }
}
