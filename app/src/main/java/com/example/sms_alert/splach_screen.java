package com.example.sms_alert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class splach_screen extends AppCompatActivity {

    ImageView imageView;
    LinearLayout image_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach_screen);

        imageView = (ImageView) findViewById(R.id.imageView);
        image_layout = (LinearLayout) findViewById(R.id.image_layout);







        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(splach_screen.this, TitleActivity.class));
                        finish();
                    }
                },
                5000
        );
    }
}
