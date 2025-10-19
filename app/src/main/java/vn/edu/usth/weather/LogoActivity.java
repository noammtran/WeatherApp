package vn.edu.usth.weather;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity implements View.OnTouchListener {

    private final int[] rootLocation = new int[2];
    private FrameLayout root;
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_move);

        root = findViewById(R.id.root_container);
        logo = findViewById(R.id.logo);

        root.setOnTouchListener(this);
        logo.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                root.getLocationOnScreen(rootLocation);
                float newX = motionEvent.getRawX() - rootLocation[0] - logo.getWidth() / 2f;
                float newY = motionEvent.getRawY() - rootLocation[1] - logo.getHeight() / 2f;

                logo.setX(newX);
                logo.setY(newY);
                return true;
            case MotionEvent.ACTION_UP:
                logo.performClick();
                return true;
            default:
                return false;
        }
    }
}