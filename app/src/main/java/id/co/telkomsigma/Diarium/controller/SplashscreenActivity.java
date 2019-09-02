package id.co.telkomsigma.Diarium.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import id.co.telkomsigma.Diarium.R;


public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        //getSupportActionBar().();

        Thread splashTread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // do nothing
                } finally {

                    Intent i = new Intent(SplashscreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        splashTread.start();
    }
}
