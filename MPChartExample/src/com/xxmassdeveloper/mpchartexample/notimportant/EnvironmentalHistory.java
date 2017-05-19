package com.xxmassdeveloper.mpchartexample.notimportant;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link com.google.gson.Gson} model for
 * a JSON Environmental History response.
 * <p/>
 * Contains environmental measurements taken over a period of time:
 * <ul>
 * <li>a {@link Date} indicates when the readings were taken</li>
 * <li>Air Quality (periodic / average)</li>
 * <li>Temperature (periodic / minimum / maximum)</li>
 * <li>Humidity (periodic / average / minimum / maximum)</li>
 * </ul>
 */
public abstract class EnvironmentalHistory {

    @SerializedName("Date")            private Date               mDate;
    @SerializedName("Aqi")             private ArrayList<Float>   mAqi;
    @SerializedName("Temperature")     private ArrayList<Integer> mTemperature;
    @SerializedName("Humidity")        private ArrayList<Integer> mHumidity;
    @SerializedName("Usage")           private ArrayList<Integer> mUsage;
    @SerializedName("TotalUsage")      private Integer            mTotalUsage;
    @SerializedName("MinTemperature")  private Integer            mMinTemperature;
    @SerializedName("MaxTemperature")  private Integer            mMaxTemperature;
    @SerializedName("AverageAqi")      private Float              mAverageAqi;

    /**
     * The date with which we associate this {@link EnvironmentalHistory} instance's readings.
     * <p/>
     * For {@link EnvironmentalHistoryDaily}, given the date <u>2017-04-18</u>,
     * we expect hourly readings for <u>2017-04-18</u>
     * from <u>00:00H</u> to <u>23:00H</u> inclusive.
     * <p/>
     * For {@link EnvironmentalHistoryWeekly}, given the date <u>2017-04-18</u>,
     * we expect daily readings for the 7 days
     * ranging from <u>2017-04-18</u> <i>(Tue)</i> to <u>2017-04-24</u> <i>(Mon)</i> inclusive.
     */
    public final Date getDate() {
        return this.mDate;
    }

    /**
     * Air-quality readings for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public final List<Float> getAqi() {
        return this.mAqi;
    }

    /**
     * Temperature readings for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public final List<Integer> getTemperature() {
        return this.mTemperature;
    }

    /**
     * Humidity readings (out of 100%) for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public final List<Integer> getHumidity() {
        return this.mHumidity;
    }

    /**
     * Minutes of recorded EC usage for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public final List<Integer> getUsage() {
        return this.mUsage;
    }

    /**
     * Total recorded EC usage (in minutes) for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : the day</li>
     * <li>{@link EnvironmentalHistoryWeekly} : the week</li>
     * </ul>
     */
    public final Integer getTotalUsage() {
        return this.mTotalUsage;
    }

    /**
     * Average air-quality for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : the day</li>
     * <li>{@link EnvironmentalHistoryWeekly} : the week</li>
     * </ul>
     */
    public final Float getAverageAqi() {
        return this.mAverageAqi;
    }

    /**
     * Minimum recorded temperature for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : the day</li>
     * <li>{@link EnvironmentalHistoryWeekly} : the week</li>
     * </ul>
     */
    public final Integer getMinTemperature() {
        return this.mMinTemperature;
    }

    /**
     * Maximum recorded temperature for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : the day</li>
     * <li>{@link EnvironmentalHistoryWeekly} : the week</li>
     * </ul>
     */
    public final Integer getMaxTemperature() {
        return this.mMaxTemperature;
    }

    /**
     * Average humidity (out of 100%) for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public abstract Integer getAverageHumidity();

    /**
     * Minimum recorded humidity (out of 100%) for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public abstract Integer getMinHumidity();

    /**
     * Maximum recorded humidity (out of 100%) for:
     * <ul>
     * <li>{@link EnvironmentalHistoryDaily} : each of the day's 24 hours</li>
     * <li>{@link EnvironmentalHistoryWeekly} : each of the week's 7 days</li>
     * </ul>
     */
    public abstract Integer getMaxHumidity();
}
