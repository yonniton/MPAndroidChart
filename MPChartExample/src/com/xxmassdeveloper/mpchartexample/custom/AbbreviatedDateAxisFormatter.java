package com.xxmassdeveloper.mpchartexample.custom;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Prints a series of integers as a series of dates.
 * <pre>
 * Given day0 = 27-Apr-2017
 *
 * ============================
 * index (int) | label (String)
 * ============================
 *       0     |     27/4
 *       1     |     28/4
 *       2     |     29/4
 *       3     |     30/4
 *       4     |      1/5
 *       5     |      2/5
 *       6     |      3/5
 * </pre>
 */
public final class AbbreviatedDateAxisFormatter implements IAxisValueFormatter {

    private final Date day0;
    private final DateFormat formatter;

    /**
     * @param day0
     *     the date that represents {@code 0} on the axis.
     * @param formatter
     *     the format applied to successive values on the axis;
     *     defaults to {@link SimpleDateFormat} with {@code "d/M"}
     *     i.e. 27-Apr-2017 prints {@code 27/4}.
     */
    public AbbreviatedDateAxisFormatter(@NonNull final Date day0, @Nullable final DateFormat formatter) {
        this.day0 = day0;
        this.formatter = formatter != null ? formatter : new SimpleDateFormat("d/M", Locale
            .getDefault());
    }

    @Override
    public String getFormattedValue(final float value, final AxisBase axis) {
        final Calendar dayN = Calendar.getInstance();
        dayN.setTime(this.day0);
        dayN.add(Calendar.DATE, Math.round(value));
        return this.formatter.format(dayN.getTime());
    }
}
