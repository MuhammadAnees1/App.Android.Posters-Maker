package com.example.postersmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ImageSaver {

    public static void saveAsImage(Context context, Bitmap bitmap) {
        // Get the directory for saving images (you can change the folder name)
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Postermaker");
        if (!directory.exists()) {
            directory.mkdirs();
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
            Toast.makeText(context, "File not found error", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IO error", Toast.LENGTH_SHORT).show();
        }
    }
}
