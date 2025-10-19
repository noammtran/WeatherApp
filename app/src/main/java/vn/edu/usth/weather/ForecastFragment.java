package vn.edu.usth.weather;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ForecastFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_forecast, container, false);
        LinearLayout list = root.findViewById(R.id.forecast_list);

        Resources res = getResources();

        String[] days  = res.getStringArray(R.array.days_short);
        String[] conds = res.getStringArray(R.array.conds_sample);
        String[] temps = res.getStringArray(R.array.forecast_temps_sample);

        int[] icons = {
                R.drawable.alert_avalanche_danger,      // icon
                R.drawable.alert_avalanche_danger,
                R.drawable.alert_avalanche_danger,
                R.drawable.alert_avalanche_danger,
                R.drawable.alert_avalanche_danger       // icon
        };

        for (int i = 0; i < days.length; i++) {
            list.addView(buildRow(
                    days[i],
                    icons[i % icons.length],
                    conds[i % conds.length],
                    temps[i % temps.length]
            ));

            View divider = new View(requireContext());
            divider.setBackgroundColor(res.getColor(R.color.forecast_divider));
            divider.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    res.getDimensionPixelSize(R.dimen.forecast_divider_thickness)));
            list.addView(divider);
        }

        return root;
    }

    private View buildRow(String day, int iconRes, String cond, String temp) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        Resources res = getResources();
        int padV = res.getDimensionPixelSize(R.dimen.space_8);
        int padH = res.getDimensionPixelSize(R.dimen.space_12);
        row.setPadding(padH, padV, padH, padV);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tvDay = new TextView(requireContext());
        tvDay.setText(day);
        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                res.getDimension(R.dimen.text_body));
        LinearLayout.LayoutParams lpDay = new LinearLayout.LayoutParams(
                res.getDimensionPixelSize(R.dimen.forecast_day_width),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvDay.setLayoutParams(lpDay);
        row.addView(tvDay);

        ImageView iv = new ImageView(requireContext());
        int iconSize = res.getDimensionPixelSize(R.dimen.icon_24);
        LinearLayout.LayoutParams lpIcon = new LinearLayout.LayoutParams(iconSize, iconSize);
        int space8 = res.getDimensionPixelSize(R.dimen.space_8);
        lpIcon.setMargins(space8, 0, space8, 0);
        iv.setLayoutParams(lpIcon);
        iv.setImageResource(iconRes);
        row.addView(iv);

        LinearLayout col = new LinearLayout(requireContext());
        col.setOrientation(LinearLayout.VERTICAL);
        col.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvCond = new TextView(requireContext());
        tvCond.setText(cond);
        tvCond.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                res.getDimension(R.dimen.text_body));

        TextView tvTemp = new TextView(requireContext());
        tvTemp.setText( temp );
        tvTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                res.getDimension(R.dimen.text_small));

        col.addView(tvCond);
        col.addView(tvTemp);
        row.addView(col);

        return row;
    }

    private int dp(int v) {
        float d = getResources().getDisplayMetrics().density;
        return Math.round(v * d);
    }
}
