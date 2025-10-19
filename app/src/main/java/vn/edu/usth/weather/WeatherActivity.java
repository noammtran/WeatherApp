package vn.edu.usth.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.graphics.Bitmap;

import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class WeatherActivity extends AppCompatActivity {

    private static final String LOG_TAG = "WeatherActivity";
    private static final String LOGO_URL = "https://usth.edu.vn/wp-content/uploads/2020/11/logo-usth-1024x381-1.png";

    private static final String LOGO_REQUEST_TAG = "usth_logo_request";
    @Nullable
    private Bitmap usthLogoBitmap;

    @Nullable
    private RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestUSTHLogo(false);

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
            requestUSTHLogo(true);
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

    private void requestUSTHLogo(boolean showUserFeedback) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        if (showUserFeedback) {
            Toast.makeText(this, R.string.refreshing_message, Toast.LENGTH_SHORT).show();
        }
        requestQueue.cancelAll(LOGO_REQUEST_TAG);

        ImageRequest request = new ImageRequest(
                LOGO_URL,
                bitmap -> {
                    updateForecastLogo(bitmap);
                    if (showUserFeedback) {
                        Toast.makeText(this, R.string.refresh_message, Toast.LENGTH_SHORT).show();
                    }
                },
                0,
                0,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.ARGB_8888,
                error -> {
                    Log.e(LOG_TAG, "Failed to download USTH logo", error);
                    if (showUserFeedback) {
                        Toast.makeText(this, R.string.refresh_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setTag(LOGO_REQUEST_TAG);
        requestQueue.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(LOGO_REQUEST_TAG);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.stop();
            requestQueue = null;
        }
    }
}