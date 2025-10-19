package vn.edu.usth.weather;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class LogoActivity extends AppCompatActivity implements View.OnTouchListener {

    private final int[] rootLocation = new int[2];
    private FrameLayout root;
    private ImageView logo;
    private GestureDetectorCompat gestureDetector;
    private OverScroller scroller;

    private final Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                logo.setX(scroller.getCurrX());
                logo.setY(scroller.getCurrY());
                root.postOnAnimation(this);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_move);

        root = findViewById(R.id.root_container);
        logo = findViewById(R.id.logo);

        scroller = new OverScroller(this);
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                stopFling();
                root.getLocationOnScreen(rootLocation);
                moveLogoTo(e);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                moveLogoTo(e2);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (root.getWidth() == 0 || root.getHeight() == 0) {
                    return false;
                }

                startFling(velocityX, velocityY);
                return true;
            }
        });

        root.setOnTouchListener(this);
        logo.setOnTouchListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopFling();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean handled = gestureDetector.onTouchEvent(motionEvent);

        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
            view.performClick();
        } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            stopFling();
        }

        return handled;
    }

    private void moveLogoTo(MotionEvent event) {
        if (root.getWidth() == 0 || root.getHeight() == 0) {
            return;
        }

        float rootX = event.getRawX() - rootLocation[0];
        float rootY = event.getRawY() - rootLocation[1];

        float halfLogoWidth = logo.getWidth() / 2f;
        float halfLogoHeight = logo.getHeight() / 2f;

        float targetX = clamp(rootX - halfLogoWidth, 0f, Math.max(0f, root.getWidth() - logo.getWidth()));
        float targetY = clamp(rootY - halfLogoHeight, 0f, Math.max(0f, root.getHeight() - logo.getHeight()));

        logo.setX(targetX);
        logo.setY(targetY);
    }

    private void startFling(float velocityX, float velocityY) {
        stopFling();

        int maxX = Math.max(0, root.getWidth() - logo.getWidth());
        int maxY = Math.max(0, root.getHeight() - logo.getHeight());

        scroller.fling(
                Math.round(logo.getX()),
                Math.round(logo.getY()),
                (int) velocityX,
                (int) velocityY,
                0,
                maxX,
                0,
                maxY
        );

        if (!scroller.isFinished()) {
            root.postOnAnimation(flingRunnable);
        }
    }

    private void stopFling() {
        root.removeCallbacks(flingRunnable);
        if (!scroller.isFinished()) {
            scroller.forceFinished(true);
        }
    }

    private static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}