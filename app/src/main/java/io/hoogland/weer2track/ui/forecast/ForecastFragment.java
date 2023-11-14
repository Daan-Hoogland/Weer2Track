package io.hoogland.weer2track.ui.forecast;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import io.hoogland.weer2track.BuildConfig;
import io.hoogland.weer2track.R;
import io.hoogland.weer2track.databinding.FragmentForecastBinding;
import io.hoogland.weer2track.db.AppDatabase;
import io.hoogland.weer2track.model.DailyForecast;
import io.hoogland.weer2track.model.dao.DailyForecastDao;
import io.hoogland.weer2track.model.response.ForecastResponse;
import io.hoogland.weer2track.service.WeatherService;
import io.hoogland.weer2track.ui.MainActivity;
import io.hoogland.weer2track.ui.currentweather.CurrentWeatherFragment;
import io.hoogland.weer2track.ui.forecast.adapter.ForecastAdapter;
import io.hoogland.weer2track.util.Constants;
import io.hoogland.weer2track.util.ForecastUtil;
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
 * Fragment class that shows the weather forecast.
 *
 * @author dan
 */
public class ForecastFragment extends Fragment {

    private FragmentForecastBinding binding;

    private static final String TAG = "forecastFragment";

    private final AppDatabase appDatabase = AppDatabase.getInstance(this.getContext());
    private final DailyForecastDao dailyForecastDao = appDatabase.dailyForecastDao();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyListTextView;
    private RecyclerView recyclerView;
    private TextView oldDataTextView;
    private TextView lastUpdatedTextView;


    public ForecastFragment() {
    }

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

        binding = FragmentForecastBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindUiElements();

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        if (MainActivity.isOldData) {
            oldDataTextView.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        loadDataIntoView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Method that binds the UI elements of the {@link ForecastFragment}
     */
    private void bindUiElements() {
        recyclerView = binding.recyclerView;
        emptyListTextView = binding.noDataLabelForecast;
        swipeRefreshLayout = binding.forecastRefreshLayout;
        oldDataTextView = binding.oldDataLabelForecast;
        lastUpdatedTextView = binding.lastUpdatedLabel;
    }

    /**
     * Loads data from the application database using {@link DailyForecastDao}.
     */
    public void loadDataIntoView() {
        dailyForecastDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<DailyForecast>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DailyForecast> dailyForecastList) {
                        Log.d(TAG, "onSuccess: creating adapter for recyclerview");
                        if (dailyForecastList.isEmpty()) {
                            emptyListTextView.setVisibility(View.VISIBLE);
                        } else {
                            ForecastAdapter adapter = new ForecastAdapter(dailyForecastList);
                            recyclerView.setAdapter(adapter);
                            emptyListTextView.setVisibility(View.INVISIBLE);
                            lastUpdatedTextView.setText(getString(R.string.label_last_modified,
                                    dailyForecastList.get(0).getModifiedAt().format(DateTimeFormatter
                                            .ofPattern(Constants.DATE_FORMAT_FULL, new Locale("nl", "NL")))));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    /**
     * Accesses the OpenWeatherMap API in order to refresh the {@link DailyForecast} data available
     * in the application database.
     */
    private void refreshData() {
        Call<ForecastResponse> fResponse = NetworkUtil.getWeatherServiceInstance()
                .getWeatherForecast(Constants.OPENWEATHER_API_LAT_VALUE, Constants.OPENWEATHER_API_LON_VALUE,
                        BuildConfig.WEATHER_API_KEY, Constants.OPENWEATHER_API_UNITS_VALUE, Constants.OPENWEATHER_API_LANGUAGE_VALUE);

        fResponse.enqueue(new Callback<ForecastResponse>() {

            /**
             * When the response is successful, convert the {@link ForecastResponse} into a
             * {@link List} of {@link DailyForecast}, delete the old rows from the application database
             * and insert it into the application database.
             *
             * @param call The original call created using {@link WeatherService}
             * @param response Response object containing data from the API call serialized as a
             * {@link ForecastResponse}
             */
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: retrieved forecast response body");
                    List<DailyForecast> forecastList = ForecastUtil.forecastResponseToDaily(response.body());
                    dailyForecastDao.deleteAll().subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: deleted all rows in database");
                            dailyForecastDao.insertWithTimestamp(forecastList).subscribeOn(Schedulers.io())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "onComplete: inserted forecast into database successfully");
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            Log.e(TAG, "onError: ", e);
                                        }
                                    });
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        }
                    });
                    ForecastAdapter adapter = new ForecastAdapter(forecastList);
                    // TODO: if time, replace dataset and call notifyDataSetChanged() instead
                    recyclerView.swapAdapter(adapter, true);
                    MainActivity.isOldData = false;
                    oldDataTextView.setVisibility(View.GONE);
                    lastUpdatedTextView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
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
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: error in network request", t);
                MainActivity.isOldData = true;
                oldDataTextView.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}