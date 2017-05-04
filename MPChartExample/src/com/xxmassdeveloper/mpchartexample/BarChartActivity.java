package com.xxmassdeveloper.mpchartexample;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import com.xxmassdeveloper.mpchartexample.notimportant.EnvironmentalHistory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;
import java.util.Locale;

public class BarChartActivity extends DemoBase
    implements OnChartValueSelectedListener {

    protected BarChart mChart;

    @Override
    protected void onCreate(final Bundle save) {
        super.onCreate(save);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_barchart);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        mChart.animateXY(1000, 2000);

        final XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1);
        xAxis.setSpaceMin(0.4f);
        xAxis.setSpaceMax(0.4f);

        mChart.getAxisRight().setEnabled(false);

        final YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setGranularity(1f);
        leftAxis.setValueFormatter(new PercentFormatter(new DecimalFormat("#")));
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);

        final Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(LegendForm.SQUARE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);

        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawIcons(!set.isDrawIconsEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : mChart.getData().getDataSets())
                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                mChart.invalidate();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart.animateXY(3000, 3000);
                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
        }
        return true;
    }

    private void setData() {
        try {

            final BufferedInputStream streamEnvHistoryWeekly = new BufferedInputStream(
                getAssets().open("EnvironmentHistory-Weekly.json")
            );
            final InputStreamReader readerEnvHistoryWeekly = new InputStreamReader(streamEnvHistoryWeekly);
            final Type typeWeeklyHistory = new TypeToken<ArrayList<EnvironmentalHistory.Weekly>>() {}.getType();
            final Gson gson = new Gson();
            final ArrayList<EnvironmentalHistory.Weekly> objEnvHistory = gson.fromJson(
                readerEnvHistoryWeekly, typeWeeklyHistory
            );
            streamEnvHistoryWeekly.close();

            final EnvironmentalHistory.Weekly env = objEnvHistory.get(1);
            final Calendar date = Calendar.getInstance();
            date.setTime(env.getDate());
            final IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
                final Calendar day_0 = date;
                @Override
                public String getFormattedValue(final float value, final AxisBase axis) {
                    final Calendar day_N = Calendar.getInstance();
                    day_N.setTime(day_0.getTime());
                    day_N.add(Calendar.DATE, Math.round(value));
                    return String.format(
                        Locale.getDefault(),
                        "%td/%<tm",
                        day_N
                    );
                }
            };

            final ArrayList<BarEntry> valsHumidity = new ArrayList<>();
            for (ListIterator<Integer> it = env.getHumidity().listIterator(); it.hasNext();) {
                final int idx = it.nextIndex();
                final Integer humidity = it.next();
                if (humidity != null) {
                    valsHumidity.add(new BarEntry(idx, humidity));
                }
            }

            mChart.getXAxis().setValueFormatter(xAxisFormatter);
            final MarkerView mv = new MarkerView(this, R.layout.custom_marker_view) {
                @Override
                public void refreshContent(final Entry e, final Highlight highlight) {
                    ((TextView) findViewById(R.id.tvContent))
                        .setText(String.format("%1.0f", e.getY()));
                    super.refreshContent(e, highlight);
                }
                @Override
                public MPPointF getOffset() {
                    return new MPPointF(-(getWidth() / 2), -getHeight());
                }
            };
            mv.setChartView(mChart);   // positioned relative to the chart
            mChart.setMarker(mv);

            final BarDataSet set1;
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(valsHumidity);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(valsHumidity, "Humidity");
                set1.setDrawIcons(false);
                set1.setDrawValues(false);
                set1.setColor(Color.parseColor("#ff077D91"));

                final ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                final BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setValueTypeface(mTfLight);

                mChart.setData(data);
                mChart.setFitBars(true);
            }

        } catch (final IOException e) {
            Log.e("assets", e.toString());
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(final Entry e, final Highlight h) {

        if (e == null)
            return;

        final RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        final MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }
}
