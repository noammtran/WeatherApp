package vn.edu.usth.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.StringRes;

public class WeatherFragment extends Fragment {

    private static final String ARG_CITY_NAME_RES = "city_name_res";

    public WeatherFragment() { }

    public static WeatherFragment newInstance(@StringRes int cityNameResId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_NAME_RES, cityNameResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        TextView tvTemp = root.findViewById(R.id.tvTemp);
        TextView tvCity = root.findViewById(R.id.tvCity);
        TextView tvDesc = root.findViewById(R.id.tvDesc);
        ImageView img   = root.findViewById(R.id.imgWeather);

        tvTemp.setText(getString(R.string.label_temp_c, 12));
        tvCity.setText(resolveCityLabel());
        tvDesc.setText(R.string.label_desc_cloudy);
        img.setImageResource(R.drawable.ic_cloudy);
        img.setOnClickListener(view -> {
            ViewGroup.LayoutParams params = view.getLayoutParams();

            int currentWidth = params.width;
            if (currentWidth == ViewGroup.LayoutParams.WRAP_CONTENT
                    || currentWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                currentWidth = view.getWidth();
            }

            int currentHeight = params.height;
            if (currentHeight == ViewGroup.LayoutParams.WRAP_CONTENT
                    || currentHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                currentHeight = view.getHeight();
            }

            if (currentWidth > 1 && currentHeight > 1) {
                params.width = Math.max(1, currentWidth / 2);
                params.height = Math.max(1, currentHeight / 2);
                view.setLayoutParams(params);
            }
        });

        return root;
    }
    private CharSequence resolveCityLabel() {
        Bundle args = getArguments();
        @StringRes int cityNameResId = R.string.label_city_default;
        if (args != null) {
            int providedResId = args.getInt(ARG_CITY_NAME_RES, 0);
            if (providedResId != 0) {
                cityNameResId = providedResId;
            }
        }

        return getString(R.string.label_city, getString(cityNameResId));
    }

}
