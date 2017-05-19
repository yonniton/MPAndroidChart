package com.xxmassdeveloper.mpchartexample.notimportant;

import com.google.common.math.DoubleMath;
import com.google.common.primitives.Ints;
import com.google.gson.annotations.SerializedName;

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
 *
 * @see EnvironmentalHistory
 */
public final class EnvironmentalHistoryWeekly extends EnvironmentalHistory {

    private Integer mAverageHumidity;
    @SerializedName("MinHumidity") private Integer mMinHumidity;
    @SerializedName("MaxHumidity") private Integer mMaxHumidity;

    /** average humidity (out of 100%) for the week */
    @Override
    public final Integer getAverageHumidity() {
        if (mAverageHumidity == null) {
            final int[] ints = Ints.toArray(getHumidity());
            mAverageHumidity = DoubleMath.roundToInt(
                DoubleMath.mean(ints),
                java.math.RoundingMode.HALF_EVEN
            );
        }
        return mAverageHumidity;
    }

    /** minimum recorded humidity (out of 100%) for the week */
    public final Integer getMinHumidity() {
        return this.mMinHumidity;
    }

    /** maximum recorded humidity (out of 100%) for the week */
    public final Integer getMaxHumidity() {
        return this.mMaxHumidity;
    }
}
