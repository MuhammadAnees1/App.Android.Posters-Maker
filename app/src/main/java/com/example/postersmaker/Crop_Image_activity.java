package com.example.postersmaker;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Crop_Image_activity extends AppCompatActivity {

    ImageView previewImageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        previewImageView1 = findViewById(R.id.previewImageView1);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        if (imageUri != null) {
            previewImageView1.setImageURI(imageUri);
        }



    }

}