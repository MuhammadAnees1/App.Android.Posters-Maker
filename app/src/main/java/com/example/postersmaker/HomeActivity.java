package com.example.postersmaker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.postersmaker.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    private ActivityResultLauncher<String> galleryLauncher;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {

            if (uri!=null) {
                // Handle the selected image from the gallery here
                openNextActivity(uri);
            }else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.newButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void openGallery() {
        galleryLauncher.launch("image/*");
    }

    private void openNextActivity(Uri imageUri) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("imageUri", imageUri.toString());
        startActivity(intent);
    }
}
