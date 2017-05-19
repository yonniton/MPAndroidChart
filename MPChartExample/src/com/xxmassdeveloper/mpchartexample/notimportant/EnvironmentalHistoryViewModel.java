package com.xxmassdeveloper.mpchartexample.notimportant;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.mikephil.charting.data.BarDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.ReplaySubject;
import io.realm.log.RealmLog;

public final class EnvironmentalHistoryViewModel {

    private final ReplaySubject<EnvironmentalHistoryDaily> mEnvHistoryDaily = ReplaySubject.createWithSize(7);
    private final ReplaySubject<EnvironmentalHistoryWeekly> mEnvHistoryWeekly = ReplaySubject.createWithSize(4);

    /** for notifying databinding adapter {@link ChartUtils#setData} */
    public final ObservableField<Pair<Date, BarDataSet>> envHistoryHumidityEntries = new ObservableField<>();

    public EnvironmentalHistoryViewModel(final android.content.res.Resources resources) {
        try {

            final BufferedInputStream streamEnvHistoryWeekly = new BufferedInputStream(
                resources.getAssets().open("EnvironmentHistory-Weekly.json")
            );
            final InputStreamReader readerEnvHistoryWeekly = new InputStreamReader(streamEnvHistoryWeekly);
            final Type typeWeeklyHistory = new TypeToken<ArrayList<EnvironmentalHistoryWeekly>>() {}.getType();
            final Gson gson = new Gson();
            final ArrayList<EnvironmentalHistoryWeekly> objEnvHistory = gson.fromJson(
                readerEnvHistoryWeekly, typeWeeklyHistory
            );
            streamEnvHistoryWeekly.close();

            for (EnvironmentalHistoryWeekly week : objEnvHistory) {
                mEnvHistoryWeekly.onNext(week);
            }
            mEnvHistoryWeekly.onComplete();

        } catch (final IOException e) {
            RealmLog.error(e, "Get Weekly Environment History failed");
        }

        getEnvHistoryWeeklyHumidityEntries().subscribe();  // playback the transformed Weekly response into the ObservableField
    }

    /**
     * {@link Observable} that emits {@link EnvironmentalHistoryDaily} elements
     * as they are parsed out of the response.
     */
    @NonNull
    public Observable<EnvironmentalHistoryDaily> getEnvHistoryDaily() {
        return mEnvHistoryDaily;
    }

    /**
     * {@link Observable} that emits {@link EnvironmentalHistoryWeekly} elements
     * as they are parsed out of the response.
     */
    @NonNull
    public Observable<EnvironmentalHistoryWeekly> getEnvHistoryWeekly() {
        return mEnvHistoryWeekly;
    }

    /**
     * Emits the corresponding day's humidity {@link BarDataSet}
     * for each emission from {@link #getEnvHistoryDaily}.
     * <p/>
     * {@link Pair#first} holds {@link EnvironmentalHistoryDaily#getDate};<br/>
     * {@link Pair#second} holds the {@link BarDataSet} corresponding to {@link EnvironmentalHistoryDaily#getHumidity}.
     */
    @NonNull
    Observable<Pair<Date, BarDataSet>> getEnvHistoryDailyHumidityEntries() {
        return mEnvHistoryDaily.map(new Function<EnvironmentalHistoryDaily, Pair<Date, BarDataSet>>() {
            @Override
            public Pair<Date, BarDataSet> apply(final EnvironmentalHistoryDaily day) throws Exception {
                final Date date = day.getDate();
                final BarDataSet dataset = ChartUtils.getFunctionEnvironmentalHistoryToHumidityData().apply(day);
                return Pair.create(date, dataset);
            }
        });
    }

    /**
     * Emits the corresponding week's humidity {@link BarDataSet}
     * for each emission from {@link #getEnvHistoryWeekly}.
     * <p/>
     * {@link Pair#first} holds {@link EnvironmentalHistoryWeekly#getDate};<br/>
     * {@link Pair#second} holds the {@link BarDataSet} corresponding to {@link EnvironmentalHistoryWeekly#getHumidity}.
     */
    @NonNull
    Observable<Pair<Date, BarDataSet>> getEnvHistoryWeeklyHumidityEntries() {

        final Observable<Pair<Date, BarDataSet>> obsDatedBarDataSets = mEnvHistoryWeekly.map(new Function<EnvironmentalHistoryWeekly, Pair<Date, BarDataSet>>() {
            @Override
            public Pair<Date, BarDataSet> apply(final EnvironmentalHistoryWeekly week) throws Exception {
                final Date weekStart = week.getDate();
                final BarDataSet dataset = ChartUtils.getFunctionEnvironmentalHistoryToHumidityData().apply(week);
                return Pair.create(weekStart, dataset);
            }
        });

        obsDatedBarDataSets.lastOrError()// TODO @jteo retrieve previous weeks on demand
            .subscribe(new DisposableSingleObserver<Pair<Date, BarDataSet>>() {
                @Override
                public void onSuccess(final Pair<Date, BarDataSet> humidity) {
                    envHistoryHumidityEntries.set(humidity);
                }
                @Override
                public void onError(final Throwable e) {
                    RealmLog.error(e.toString(), e);
                }
            });

        return obsDatedBarDataSets;
    }
}
