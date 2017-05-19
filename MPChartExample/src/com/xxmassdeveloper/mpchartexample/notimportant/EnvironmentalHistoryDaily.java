package com.xxmassdeveloper.mpchartexample.notimportant;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import com.google.gson.annotations.SerializedName;

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
 *
 * @see EnvironmentalHistory
 */
public final class EnvironmentalHistoryDaily extends EnvironmentalHistory {

    @SerializedName("AverageHumidity") private Integer mAverageHumidity;
    private Integer mMinHumidity;
    private Integer mMaxHumidity;

    /** average humidity (out of 100%) for the day */
    @Override
    public final Integer getAverageHumidity() {
        return this.mAverageHumidity;
    }

    /** minimum recorded humidity (out of 100%) for the day */
    @Override
    public final Integer getMinHumidity() {
        if (mMinHumidity == null) {
            mMinHumidity = FluentIterable.from(
                Ordering.natural()
                    .sortedCopy(getHumidity())
            ).first().orNull();
        }
        return mMinHumidity;
    }

    /** maximum recorded humidity (out of 100%) for the day */
    @Override
    public final Integer getMaxHumidity() {
        if (mMaxHumidity == null) {
            mMaxHumidity = FluentIterable.from(
                Ordering.natural().reverse()
                    .sortedCopy(getHumidity())
            ).first().orNull();
        }
        return mMaxHumidity;
    }
}
