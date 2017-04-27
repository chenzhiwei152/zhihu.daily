package com.chen.common.View.ChartView;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.chen.common.Utils.LogUtils;
import com.chen.develop.zhihudaily.R;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;


public class LineCardOne extends CardController {


    private LineChartView mChart;


    private final Context mContext;
    private int MaxValure = 60;
    private int MinValure = 0;
    private Runnable chartAction;
    private String[] mLabels = {"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep"};
    private float[][] mValues = {{3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f},
            {4.5f, 2.5f, 2.5f, 9f, 4.5f, 9.5f, 5f, 8.3f, 1.8f}};

    private Tooltip mTip;

    private Runnable mBaseAction;


    public LineCardOne(LineChartView card, Context context) {
        super(card);

        mContext = context;
        mChart = (LineChartView) card.findViewById(R.id.lc_chart);
    }

    public LineCardOne(LineChartView card, Context context, String[] mLabels, float[][] mValues) {
        super(card);
        mContext = context;
        this.mLabels = mLabels;
        this.mValues = mValues;
        mChart = (LineChartView) card.findViewById(R.id.lc_chart);
    }

    public void SetWeatherBase(String[] mLabels, float[][] mValues) {
        this.mLabels = mLabels;
        this.mValues = mValues;
        update();
    }

    @Override
    public void show(Runnable action) {
        super.show(action);

        // Tooltip
        mTip = new Tooltip(mContext, R.layout.linechart_three_tooltip, R.id.value);

        ((TextView) mTip.findViewById(R.id.value))
                .setTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Semibold.ttf"));

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(65), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }

        mChart.setTooltips(mTip);

        // Data
        LineSet dataset = new LineSet(mLabels, mValues[0]);
        dataset.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#00000000"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(4)
                .setDashed(new float[]{10f, 10f})
                .beginAt(0);
        mChart.addData(dataset);

        dataset = new LineSet(mLabels, mValues[0]);
        dataset.setColor(Color.parseColor("#b3b5bb"))
                .setFill(Color.parseColor("#00000000"))
                .setDotsColor(Color.parseColor("#ffc755"))
                .setThickness(4)
                .endAt(1);
        mChart.addData(dataset);

        // Chart
        mChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(getMinValue(), getMaxValue())
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(false)
                .setYAxis(false);

        mBaseAction = action;
        chartAction = new Runnable() {
            @Override
            public void run() {
                mBaseAction.run();
                mTip.prepare(mChart.getEntriesArea(0).get(0), mValues[0][0]);
                mChart.showTooltip(mTip, true);
            }
        };
        StartAnimation();

    }

    private void StartAnimation() {

        Animation anim = new Animation()
                .setEasing(new BounceEase())
                .setEndAction(chartAction);
        mChart.show(anim);
    }

    private int getMaxValue() {
        float max = mValues[0][0]; //设二维数组中的第一个为最大的值
//        for (int i = 0; i < mValues.length; i++) {
        for (int j = 0; j < mValues[0].length; j++) {
            if (mValues[0][j] > max) { //如果该数组中还有比最大值都大的,那么它就是最大的
                max = mValues[0][j];
            }
        }
//        }
        MaxValure = (int) max + 5;
        LogUtils.e("MaxValure:" + MaxValure);
        return MaxValure;
    }


    private int getMinValue() {
        float min = mValues[0][0]; //设二维数组中的第一个为最小的值
//        for (int i = 0; i < mValues.length; i++) {
        for (int j = 0; j < mValues[1].length; j++) {
            if (mValues[0][j] < min) { //如果该数组中还有比最小值都小的,那么它就是最小的
                min = mValues[0][j];
            }
        }
//        }
        MinValure = (int) min - 5;
        LogUtils.e("MinValure:" + MinValure);
        return MinValure;
    }

    @Override
    public void update() {
        super.update();

        mChart.dismissAllTooltips();
//        if (firstStage) {
//            mChart.updateValues(0, mValues[1]);
//            mChart.updateValues(1, mValues[1]);
//        } else {
        mChart.updateValues(0, mValues[0]);
        mChart.updateValues(1, mValues[0]);
//        }
        mChart.getChartAnimation().setEndAction(mBaseAction);
        mChart.notifyDataUpdate();
        StartAnimation();
    }


//    @Override
//    public void dismiss(Runnable action) {
//        super.dismiss(action);
//
//        mChart.dismissAllTooltips();
//        mChart.dismiss(new Animation()
//                .setEasing(new BounceEase())
//                .setEndAction(action));
//    }

}
