package com.xxmassdeveloper.mpchartexample.notimportant;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.xxmassdeveloper.mpchartexample.R;
import com.xxmassdeveloper.mpchartexample.custom.AbbreviatedDateAxisFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Function;

/**
 * Utilities dealing with {@link com.github.mikephil.charting.charts.Chart}.
 */
public final class ChartUtils {

    private ChartUtils() {}

    /**
     * {@link MarkerView} implementation that prints selected {@link BarEntry}s
     * from {@link EnvironmentalHistoryViewModel#getEnvHistoryDailyHumidityEntries}
     * and {@link EnvironmentalHistoryViewModel#getEnvHistoryWeeklyHumidityEntries}.
     * Specifically, it prints out the EC-usage value for that {@link BarEntry}.
     */
    public static MarkerView getUsageHistoryMarker(final Context ctx) {
        return new MarkerView(ctx, R.layout.custom_marker_view) {
            @Override
            public void refreshContent(final Entry envEntry, final Highlight highlight) {
                final Integer totalMinutes = (Integer) envEntry.getData();
                final StringBuilder sb = new StringBuilder("PURIFIER ACTIVE : ");
                final long hours = TimeUnit.MINUTES.toHours(totalMinutes);
                final long minutes = totalMinutes - TimeUnit.HOURS.toMinutes(hours);
                if (hours > 0) {
                    sb.append(hours).append("H ");
                }
                sb.append(minutes).append('M');
                ((TextView) findViewById(R.id.tvContent))
                    .setText(sb);
                super.refreshContent(envEntry, highlight);
            }
            @Override
            public MPPointF getOffset() {        // position relative to bar
                return new MPPointF(-(getWidth() / 2), -getHeight());
            }
        };
    }

    /**
     * Custom Binding for updating a {@link BarChart}
     * with historical humidity data.
     *
     * <pre>{@code
     * <data>
     *     <variable
     *         name="vm"
     *         type="com.xxmassdeveloper.mpchartexample.notimportant.EnvironmentalHistoryViewModel"/>
     * </data>
     * <com.github.mikephil.charting.charts.BarChart
     *     ...
     *     app:envHistoryHumidity="@{vm.envHistoryHumidityEntries}"/>
     * }</pre>
     *
     * @param barChart
     *     several properties of the specified {@link BarChart} will be updated:
     *     <ul>
     *     <li>X-axis - the timestamps of the new readings</li>
     *     <li>marker - renders corresponding usage readings</li>
     *     <li>data - the new humidity readings</li>
     *     </ul>
     * @param newHumidity
     *     <ul>
     *     <li>{@link Pair#first} : the {@link Date} corresponding to
     *     {@link EnvironmentalHistory#getDate}</li>
     *     <li>{@link Pair#second} : {@link BarDataSet} corresponding to
     *     {@link EnvironmentalHistory#getHumidity}</li>
     *     </ul>
     *
     * @see EnvironmentalHistoryViewModel#getEnvHistoryDailyHumidityEntries
     * @see EnvironmentalHistoryViewModel#getEnvHistoryWeeklyHumidityEntries
     * @see EnvironmentalHistoryViewModel#envHistoryHumidityEntries
     */
    @BindingAdapter("envHistoryHumidity")
    public static void setData(@NonNull final BarChart barChart, @Nullable final Pair<Date, BarDataSet> newHumidity) {

        if (newHumidity == null) {
            return;
        }

        barChart.getXAxis().setValueFormatter(
            new AbbreviatedDateAxisFormatter(newHumidity.first, null)
        );

        final Context ctx = barChart.getContext();
        final MarkerView mv = getUsageHistoryMarker(ctx);
        mv.setChartView(barChart);     // position relative to chart
        barChart.setMarker(mv);

        final BarDataSet dataset;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            dataset = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            dataset.setValues(newHumidity.second.getValues());
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            dataset = newHumidity.second;
            dataset.setDrawIcons(false);
            dataset.setDrawValues(false);
            dataset.setColor(ctx.getResources().getColor(android.R.color.holo_blue_dark));

            final ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataset);

            barChart.setData(new BarData(dataSets));
            barChart.setFitBars(true);
        }
    }

    /**
     * Transforms an {@link EnvironmentalHistory} into a {@link BarDataSet}.
     * <p/>
     * Populates the {@link BarDataSet} with readings from:
     * <ul>
     * <li>{@link EnvironmentalHistory#getHumidity}</li>
     * <li>{@link EnvironmentalHistory#getUsage}</li>
     * </ul>
     */
    public static Function<EnvironmentalHistory, BarDataSet> getFunctionEnvironmentalHistoryToHumidityData() {
        return new Function<EnvironmentalHistory, BarDataSet>() {
            @Override
            public BarDataSet apply(final EnvironmentalHistory period) throws Exception {
                final Date date = period.getDate();
                final BarDataSet dataset = new BarDataSet(
                    new ArrayList<BarEntry>(),
                    String.format(
                        Locale.getDefault(),
                        "Humidity-%tY%<tm%<td",
                        date
                    )
                );
                for (ListIterator<Integer> it = period.getHumidity()
                    .listIterator(); it.hasNext(); ) {
                    final int idx = it.nextIndex();
                    final Integer humidity = it.next();
                    if (humidity != null) {
                        dataset.addEntry(new BarEntry(
                            idx,                           // 0-based period-index; for Daily history represents hour-of-day; for Weekly history represents day-of-week
                            humidity,                      // the humidity value for that period
                            period.getUsage().get(idx)     // the corresponding usage-reading for that period
                        ));
                    }
                }
                return dataset;
            }
        };
    }
}
