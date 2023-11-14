package io.hoogland.weer2track.ui.currentweather;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import io.hoogland.weer2track.BuildConfig;
import io.hoogland.weer2track.R;
import io.hoogland.weer2track.databinding.FragmentCurrentweatherBinding;
import io.hoogland.weer2track.db.AppDatabase;
import io.hoogland.weer2track.model.CurrentWeather;
import io.hoogland.weer2track.model.dao.CurrentWeatherDao;
import io.hoogland.weer2track.model.response.CurrentWeatherResponse;
import io.hoogland.weer2track.service.WeatherService;
import io.hoogland.weer2track.ui.MainActivity;
import io.hoogland.weer2track.ui.forecast.ForecastFragment;
import io.hoogland.weer2track.util.Constants;
import io.hoogland.weer2track.util.NetworkUtil;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment class that shows the current weather.
 *
 * @author dan
 */
public class CurrentWeatherFragment extends Fragment {

    private FragmentCurrentweatherBinding binding;
    private final AppDatabase appDatabase = AppDatabase.getInstance(this.getContext());
    private final CurrentWeatherDao currentWeatherDao = appDatabase.currentWeatherDao();
    private static final String TAG = "currentWeatherFragment";

    private TextView dateTimeView;
    private TextView currentTempView;
    private TextView feelsLikeTempView;
    private TextView descriptionView;
    private ImageView iconView;
    private TextView noDataView;
    private TextView oldDataView;
    private SwipeRefreshLayout refreshLayout;

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CurrentWeatherViewModel currentWeatherViewModel =
                new ViewModelProvider(this).get(CurrentWeatherViewModel.class);

        binding = FragmentCurrentweatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindUiElements();

        loadDataIntoView(currentWeatherViewModel, inflater.getContext());

        refreshLayout.setOnRefreshListener(() -> refreshData(currentWeatherViewModel, inflater.getContext()));

        currentWeatherViewModel.getDateTime().observe(getViewLifecycleOwner(), dateTimeView::setText);
        currentWeatherViewModel.getCurrentTemp().observe(getViewLifecycleOwner(), currentTempView::setText);
        currentWeatherViewModel.getFeelsLike().observe(getViewLifecycleOwner(), feelsLikeTempView::setText);
        currentWeatherViewModel.getDescription().observe(getViewLifecycleOwner(), descriptionView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Method that binds the UI elements of the {@link CurrentWeatherFragment}
     */
    private void bindUiElements() {
        dateTimeView = binding.dateTime;
        currentTempView = binding.currentTemp;
        feelsLikeTempView = binding.currentFeelsLike;
        descriptionView = binding.currentDescription;
        iconView = binding.weatherIcon;
        noDataView = binding.noDataLabelCurrent;
        oldDataView = binding.oldDataLabelCurrent;
        refreshLayout = binding.currentWeatherRefreshLayout;

    }

    /**
     * Loads data from the application database using {@link CurrentWeatherDao}.
     *
     * @param currentWeatherViewModel ViewModel that contains populates the Views inside the fragment
     * @param context                 Context used to access String resources
     */
    private void loadDataIntoView(CurrentWeatherViewModel currentWeatherViewModel, Context context) {
        appDatabase.currentWeatherDao().getCurrentWeather().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CurrentWeather>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CurrentWeather weather) {
                        if (MainActivity.isOldData) {
                            oldDataView.setVisibility(View.VISIBLE);
                        }
                        currentWeatherViewModel.populateWeatherData(weather, context);
                        Picasso.get().load(String.format(Constants.OPENWEATHER_ICON_URL, weather.getIcon())).into(iconView);
                        if (refreshLayout.isRefreshing()) {
                            refreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        oldDataView.setVisibility(View.VISIBLE);
                        noDataView.setVisibility(View.VISIBLE);
                        dateTimeView.setVisibility(View.GONE);
                        currentTempView.setVisibility(View.GONE);
                        feelsLikeTempView.setVisibility(View.GONE);
                        descriptionView.setVisibility(View.GONE);
                        iconView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Accesses the OpenWeatherMap API in order to refresh the {@link CurrentWeather} data available
     * in the application database.
     *
     * @param currentWeatherViewModel ViewModel that contains populates the Views inside the fragment
     * @param context                 Context used to access String resources
     */
    private void refreshData(CurrentWeatherViewModel currentWeatherViewModel, Context context) {
        Call<CurrentWeatherResponse> wResponse = NetworkUtil.getWeatherServiceInstance()
                .getCurrentWeather(Constants.OPENWEATHER_API_LAT_VALUE, Constants.OPENWEATHER_API_LON_VALUE,
                        BuildConfig.WEATHER_API_KEY, Constants.OPENWEATHER_API_UNITS_VALUE, Constants.OPENWEATHER_API_LANGUAGE_VALUE);

        wResponse.enqueue(new Callback<CurrentWeatherResponse>() {

            /**
             * When the response is successful, convert the {@link CurrentWeatherResponse} into a
             * {@link CurrentWeather} object and insert it into the application database.
             *
             * @param call The original call created using {@link WeatherService}
             * @param response Response object containing data from the API call serialized as a
             * {@link CurrentWeatherResponse}
             */
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                // process data to Room objects
                CurrentWeather currentWeather = new CurrentWeather(response.body());
                Log.d(TAG, "onResponse: retrieved current weather response body");
                // Save data from response in Room
                currentWeatherDao.insert(currentWeather).subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: inserted current weather into database successfully");
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.e(TAG, "onError: ", e);
                            }
                        });
                MainActivity.isOldData = false;
                oldDataView.setVisibility(View.GONE);

                currentWeatherViewModel.populateWeatherData(currentWeather, context);
                Picasso.get().load(String.format(Constants.OPENWEATHER_ICON_URL, currentWeather.getIcon())).into(iconView);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            /**
             * Called when the network call fails. Sets the variable that {@link CurrentWeatherFragment}
             * and {@link ForecastFragment} use in order to show the no internet connection text, as
             * well as setting certain UI elements to visible in order to indicate a missing internet
             * connection.
             *
             * @param call The original call created using {@link WeatherService}
             * @param t Information as to why the call failed
             */
            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                MainActivity.isOldData = true;
                oldDataView.setVisibility(View.VISIBLE);
                dateTimeView.setText(getString(R.string.label_last_modified, dateTimeView.getText()));
                refreshLayout.setRefreshing(false);
            }
        });
    }
}