package vn.edu.usth.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {

    private static final String LOG_TAG = "WeatherActivity";
    private static final String LOGO_URL = "https://usth.edu.vn/wp-content/uploads/2020/11/logo-usth-1024x381-1.png";

    @Nullable
    private Bitmap usthLogoBitmap;

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
    @Nullable
    public Bitmap getLatestLogoBitmap() {
        return usthLogoBitmap;
    }

    public void updateForecastLogo(@Nullable Bitmap bitmap) {
        usthLogoBitmap = bitmap;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment == null) {
                continue;
            }
            if (fragment instanceof WeatherAndForecastFragment) {
                ((WeatherAndForecastFragment) fragment).updateForecastLogo(bitmap);
            }
        }
    }

    private static class RefreshWeatherTask extends AsyncTask<Void, Void, Bitmap> {

        private final WeakReference<WeatherActivity> activityReference;

        RefreshWeatherTask(WeatherActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(LOGO_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setInstanceFollowRedirects(true);
                connection.setDoInput(true);
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to download USTH logo", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            WeatherActivity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            if (bitmap != null) {
                activity.updateForecastLogo(bitmap);
                Toast.makeText(activity, R.string.refresh_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, R.string.refresh_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }
}