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

public class WeatherFragment extends Fragment {

    public WeatherFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        TextView tvTemp = root.findViewById(R.id.tvTemp);
        tvTemp.setText(getString(R.string.label_temp_c, 12));
        TextView tvCity = root.findViewById(R.id.tvCity);
        TextView tvDesc = root.findViewById(R.id.tvDesc);
        ImageView img   = root.findViewById(R.id.imgWeather);

        tvTemp.setText("12°C");
        tvCity.setText("Paris");
        tvDesc.setText("Cloudy");
        img.setImageResource(R.drawable.alert_avalanche_danger);

        return root;
    }
}
