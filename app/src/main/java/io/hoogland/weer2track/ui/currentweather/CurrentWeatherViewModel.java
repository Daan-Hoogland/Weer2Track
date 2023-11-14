package io.hoogland.weer2track.ui.currentweather;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import io.hoogland.weer2track.R;
import io.hoogland.weer2track.model.CurrentWeather;
import io.hoogland.weer2track.ui.MainActivity;
import io.hoogland.weer2track.util.Constants;

/**
 * ViewModel responsible for populating data in the {@link CurrentWeatherFragment}
 *
 * @author dan
 */
public class CurrentWeatherViewModel extends ViewModel {

    private final MutableLiveData<String> mIcon;
    private final MutableLiveData<String> mDescription;
    private final MutableLiveData<String> mDateTime;
    private final MutableLiveData<String> mCurrentTemp;
    private final MutableLiveData<String> mFeelsLike;
    private final MutableLiveData<String> mSun;


    /**
     * Assigns value to most of the UI elements.
     *
     * @param weather {@link CurrentWeather} that contains the data to be displayed
     * @param context Context used to access String resources
     */
    public void populateWeatherData(CurrentWeather weather, Context context) {
        mDescription.setValue(StringUtils.capitalize(weather.getDescription()));
        if (MainActivity.isOldData) {
            mDateTime.setValue(context.getString(R.string.label_last_modified, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_FULL,
                    new Locale("nl", "NL")).format(weather.getDateTime())));
        } else {
            mDateTime.setValue(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_FULL,
                    new Locale("nl", "NL")).format(weather.getDateTime()));
        }
        mCurrentTemp.setValue(context.getString(R.string.label_current_temp, (int) Math.round(weather.getTemp())));
        mFeelsLike.setValue(context.getString(R.string.label_feels_like_temp, (int) Math.round(weather.getTemp())));
    }

    public CurrentWeatherViewModel() {
        mIcon = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
        mDateTime = new MutableLiveData<>();
        mCurrentTemp = new MutableLiveData<>();
        mFeelsLike = new MutableLiveData<>();
        mSun = new MutableLiveData<>();
    }

    public LiveData<String> getIcon() {
        return mIcon;
    }

    public LiveData<String> getDescription() {
        return mDescription;
    }

    public LiveData<String> getDateTime() {
        return mDateTime;
    }

    public LiveData<String> getCurrentTemp() {
        return mCurrentTemp;
    }

    public LiveData<String> getFeelsLike() {
        return mFeelsLike;
    }

    public LiveData<String> getSun() {
        return mSun;
    }
}