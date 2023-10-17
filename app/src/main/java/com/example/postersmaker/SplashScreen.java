package com.example.postersmaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = findViewById(R.id.splash_image);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        fadeIn.setStartOffset(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(2000);
        fadeOut.setStartOffset(3000);

        // Apply the animations to the ImageView
        imageView.startAnimation(fadeIn);

        // Using a Handler to delay the execution of the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(fadeOut);
            }
        }, SPLASH_TIME_OUT);

        // Start the next activity after the splash screen duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close the splash activity so the user won't be able to go back to it
            }
        }, SPLASH_TIME_OUT + 4000); // Add the duration of both animations to the SPLASH_TIME_OUT
    }
}
