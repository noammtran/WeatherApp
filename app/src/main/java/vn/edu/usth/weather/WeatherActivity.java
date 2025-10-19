package vn.edu.usth.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager pager = findViewById(R.id.pager);
        WeatherPagerAdapter adapter = new WeatherPagerAdapter(getSupportFragmentManager(), this);
        pager.setOffscreenPageLimit(adapter.getCount() - 1);
        pager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Toast.makeText(this, R.string.refreshing_message, Toast.LENGTH_SHORT).show();

            new RefreshWeatherTask(this).execute();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static class RefreshWeatherTask extends AsyncTask<Void, Void, String> {

        private final WeakReference<WeatherActivity> activityReference;

        RefreshWeatherTask(WeatherActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            WeatherActivity activity = activityReference.get();
            return activity != null ? activity.getString(R.string.refresh_message) : null;
        }

        @Override
        protected void onPostExecute(String message) {
            WeatherActivity activity = activityReference.get();
            if (activity != null && message != null) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}