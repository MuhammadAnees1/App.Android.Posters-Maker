package com.example.postersmaker;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.dhaval2404.imagepicker.ImagePicker;
public class Image_pick extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        openGallery();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            openNextActivity(imageUri);
        }
    }
   public void openGallery() {
       ImagePicker.with(this)
               .crop()
               .compress(1024)
               .maxResultSize(1080, 1080)
               .start(PICK_IMAGE_REQUEST);
    }
    private void openNextActivity(Uri imageUri) {
        Intent intent = new Intent(this, Crop_Image_activity.class);
        intent.putExtra("imageUri", imageUri.toString());
        setResult(RESULT_OK, intent);
        startActivity(intent);}
}