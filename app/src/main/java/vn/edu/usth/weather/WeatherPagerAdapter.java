package vn.edu.usth.weather;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;
    private final String[] titles;

    public WeatherPagerAdapter(@NonNull FragmentManager fm, Context ctx) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = ctx;
        titles = new String[] {
                context.getString(R.string.city_hanoi),
                context.getString(R.string.city_paris),
                context.getString(R.string.city_toulouse),

        };
    }

    @Override
    public Fragment getItem(int position) {
        return new WeatherAndForecastFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}