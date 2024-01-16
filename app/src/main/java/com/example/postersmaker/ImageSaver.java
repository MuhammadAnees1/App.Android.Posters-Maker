package com.example.postersmaker;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import static java.nio.file.Files.createDirectory;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class ImageSaver {

    public static void saveAsImage(Context context, Bitmap bitmap) {
        // Get the directory for saving images (you can change the folder name)
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Postermaker");
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Toast.makeText(context, "Directory created successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Directory creation failed
                Toast.makeText(context, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // Create a unique filename based on the current timestamp
        String fileName = "postermaker_image_" + System.currentTimeMillis() + ".png";

        // Create a File object for the image file
        File imageFile = new File(directory, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);}
            fileOutputStream.flush();
            fileOutputStream.close();

            // Notify the system about the new file so it shows up in the gallery
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()
            }, null, null);

            Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IO error", Toast.LENGTH_SHORT).show();
        }
    }




}
