package com.xxmassdeveloper.mpchartexample.notimportant;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link com.google.gson.Gson} model for
 * a JSON Environmental History response.
 */
public class EnvironmentalHistory {

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
     * <pre>
     * ### Obtain DAILY Sample using: ###
     *
     * URL : https://api.cpstage.dyson.com/v1/messageprocessor/devices/NC8-UK-HHA0006A/EnvironmentDailyHistory
     * Headers : {
     *   Authorization=Basic ZTY0YjBiYzEtNTNiYy00YjFmLTkyOTUtMGJmZDI5ODQ1NzY5OjVjNGwyUjY0RU81RnFyRWV3TXdaTUVrWWc1M1lUUi9iYWMzMEwxU2hjRkkrOGVVTnhQWldldnp0MkxlREtYUytZTHJ6aG1Pa1ZOc1hSNWdxRWpRalp3PT0=,
     *   Test-Session-Token=dyson_test_token_a7c53144-d066-4a67-b2d4-d11f49670b28
     * }
     * </pre>
     */
    public static final class Daily extends EnvironmentalHistory {

        @SerializedName("AverageHumidity") private Integer mAverageHumidity;

        /** the date for which the day's environmental readings are associated */
        public final Date getDate() {
            return super.mDate;
        }

        /**
         * Air-quality readings for each of the day's 24 hours.
         *
         * @see AirQualityLevel
         */
        public final List<Float> getAqi() {
            return super.mAqi;
        }

        /** @see TemperatureUtils */
        public final List<Integer> getTemperature() {
            return super.mTemperature;
        }

        /** humidity readings (out of 100%) for each of the day's 24 hours */
        public final List<Integer> getHumidity() {
            return super.mHumidity;
        }

        /** minutes of recorded EC usage for each of the day's 24 hours */
        public final List<Integer> getUsage() {
            return super.mUsage;
        }

        /** total recorded EC usage for the day (minutes) */
        public final Integer getTotalUsage() {
            return super.mTotalUsage;
        }

        /** minimum recorded temperature for the day */
        public final Integer getMinTemperature() {
            return super.mMinTemperature;
        }

        /** maximum recorded temperature for the day */
        public final Integer getMaxTemperature() {
            return super.mMaxTemperature;
        }

        /** average humidity (out of 100%) for the day */
        public final Integer getAverageHumidity() {
            return this.mAverageHumidity;
        }

        /** average air-quality for the day */
        public final Float getAverageAqi() {
            return super.mAverageAqi;
        }
    }

    /**
     * <pre>
     * ### Obtain WEEKLY Sample using: ###
     *
     * URL : https://api.cpstage.dyson.com/v1/messageprocessor/devices/NC8-UK-HHA0006A/EnvironmentWeeklyHistory
     * Headers : {
     *   Authorization=Basic ZTY0YjBiYzEtNTNiYy00YjFmLTkyOTUtMGJmZDI5ODQ1NzY5OjVjNGwyUjY0RU81RnFyRWV3TXdaTUVrWWc1M1lUUi9iYWMzMEwxU2hjRkkrOGVVTnhQWldldnp0MkxlREtYUytZTHJ6aG1Pa1ZOc1hSNWdxRWpRalp3PT0=,
     *   Test-Session-Token=dyson_test_token_a7c53144-d066-4a67-b2d4-d11f49670b28
     * }
     * </pre>
     */
    public static final class Weekly extends EnvironmentalHistory {

        @SerializedName("MinHumidity") private Integer mMinHumidity;
        @SerializedName("MaxHumidity") private Integer mMaxHumidity;

        /**
         * The date for which the week's environmental readings are associated.
         * Given the date {@code 2017-04-18}, the week is defined as
         * the 7 days ranging from {@code 2017-04-18} <i>(Tue)</i>
         * to {@code 2017-04-24} <i>(Mon)</i> inclusive.
         */
        public final Date getDate() {
            return super.mDate;
        }

        /**
         * Air-quality readings for each of the week's 7 days.
         *
         * @see AirQualityLevel
         */
        public final List<Float> getAqi() {
            return super.mAqi;
        }

        /** @see TemperatureUtils */
        public final List<Integer> getTemperature() {
            return super.mTemperature;
        }

        /** humidity readings (out of 100%) for each of the week's 7 days */
        public final List<Integer> getHumidity() {
            return super.mHumidity;
        }

        /** recorded EC usage for each of the week's 7 days (minutes) */
        public final List<Integer> getUsage() {
            return super.mUsage;
        }

        /** total recorded EC usage for the week (minutes) */
        public final Integer getTotalUsage() {
            return super.mTotalUsage;
        }

        /** minimum recorded temperature for the week */
        public final Integer getMinTemperature() {
            return super.mMinTemperature;
        }

        /** maximum recorded temperature for the week */
        public final Integer getMaxTemperature() {
            return super.mMaxTemperature;
        }

        /** minimum recorded humidity (out of 100%) for the week */
        public final Integer getMinHumidity() {
            return this.mMinHumidity;
        }

        /** maximum recorded humidity (out of 100%) for the week */
        public final Integer getMaxHumidity() {
            return this.mMaxHumidity;
        }

        /** average air-quality for the week */
        public final Float getAverageAqi() {
            return super.mAverageAqi;
        }
    }
}
