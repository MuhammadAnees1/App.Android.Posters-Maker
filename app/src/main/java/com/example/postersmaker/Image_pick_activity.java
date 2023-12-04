package com.example.postersmaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class Image_pick_activity extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        openGallery();
    }

    private void openGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICK_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();

            // Proceed with your logic
            openNextActivity(imageUri);
        }
    }

    private void openNextActivity(Uri imageUri) {
        Intent intent = new Intent(this, Crop_Image_activity.class);
        intent.putExtra("imageUri", imageUri.toString());
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}
