package io.hoogland.weer2track.ui.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.hoogland.weer2track.R;
import io.hoogland.weer2track.model.DailyForecast;
import io.hoogland.weer2track.util.Constants;

/**
 * Adapter to display weather forecasts in a {@link RecyclerView}
 *
 * @author dan
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<DailyForecast> forecastDataSet;

    /**
     * Internal class for assigning values to each item in the {@link RecyclerView}
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIcon;
        private final TextView mDescription;
        private final TextView mDate;
        private final TextView mMinTemp;
        private final TextView mMaxTemp;
        private final TextView mPrecipitationChance;


        /**
         * Constructor for the {@link ViewHolder}.
         *
         * @param v View the {@link ViewHolder} is part of
         */
        public ViewHolder(View v) {
            super(v);
            mIcon = v.findViewById(R.id.forecastWeatherIcon);
            mDescription = v.findViewById(R.id.forecastWeatherDescription);
            mDate = v.findViewById(R.id.forecastDate);
            mMinTemp = v.findViewById(R.id.forecastMinTemp);
            mMaxTemp = v.findViewById(R.id.forecastMaxTemp);
            mPrecipitationChance = v.findViewById(R.id.forecastPrecipitation);
        }

        public ImageView getIcon() {
            return mIcon;
        }

        public TextView getDescription() {
            return mDescription;
        }

        public TextView getDate() {
            return mDate;
        }

        public TextView getMinTemp() {
            return mMinTemp;
        }

        public TextView getMaxTemp() {
            return mMaxTemp;
        }

        public TextView getPrecipitationChance() {
            return mPrecipitationChance;
        }

    }

    /**
     * Constructor for the adapter. Sorts the {@link List} of {@link DailyForecast} by date before
     * being displayed.
     *
     * @param forecastDataSet {@link List} of {@link DailyForecast} to be displayed
     */
    public ForecastAdapter(List<DailyForecast> forecastDataSet) {
        forecastDataSet.sort(Comparator.comparing(DailyForecast::getDate));
        this.forecastDataSet = forecastDataSet;
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_row_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Sets the values of the Views bound to the {@link ViewHolder}.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getDescription().setText(StringUtils.capitalize(forecastDataSet.get(position).getDescription()));
        holder.getDate().setText(StringUtils.capitalize(forecastDataSet.get(position).getDate()
                .format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT, new Locale("nl", "NL")))));
        holder.getMinTemp().setText(holder.itemView.getContext().getString(R.string.label_mintemp, Constants.DECIMAL_FORMAT.format(forecastDataSet.get(position).getMinTemp())));
        holder.getMaxTemp().setText(holder.itemView.getContext().getString(R.string.label_maxtemp, Constants.DECIMAL_FORMAT.format(forecastDataSet.get(position).getMaxTemp())));
        holder.getPrecipitationChance().setText(holder.itemView.getContext().getString(R.string.label_precipitation, Math.round(forecastDataSet.get(position).getPrecipitationChance() * 100)));
        Picasso.get().load(String.format(Constants.OPENWEATHER_ICON_URL, forecastDataSet.get(position).getIcon())).into(holder.getIcon());
    }

    /**
     * Gets the number of views to be displayed by the adapter.
     *
     * @return Number of items in the dataset
     */
    @Override
    public int getItemCount() {
        return forecastDataSet.size();
    }
}
