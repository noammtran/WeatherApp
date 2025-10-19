package vn.edu.usth.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] titles ;

    public WeatherPagerAdapter(@NonNull FragmentManager fm, Context ctx) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        titles = new String[] {
                ctx.getString(R.string.city_hanoi),
                ctx.getString(R.string.city_paris),
                ctx.getString(R.string.city_toulouse),

        };
    }
    @Override public CharSequence getPageTitle(int position) {return titles[position];}
}