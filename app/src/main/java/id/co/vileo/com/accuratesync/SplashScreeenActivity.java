package id.co.vileo.com.accuratesync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suhe on 6/13/2016.
 */
public class SplashScreeenActivity extends AppCompatActivity {
    int delay  = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
        Timer RunSplash = new Timer();
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(SplashScreeenActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        RunSplash.schedule(ShowSplash, delay);
    }
}
