package com.example.postersmaker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BackgroundApi {

    private static final String BASE_URL = "https://poster-maker.sftester.pw/api/V1/backgrounds";

    public static void getBackgroundListFromApi(final Context context, final OnBackgroundListReceivedListener listener) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<Background> backgroundList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject backgroundObject = jsonArray.getJSONObject(i);
                            String backgroundImageUrl = backgroundObject.getString("image_path");
                            Background background = new Background();
                            String filePath = downloadBackground(context, backgroundImageUrl);
                            if (filePath != null) {
                                background.setName("Background " + backgroundObject.getInt("id"));
                                background.setFilePath(filePath);
                                backgroundList.add(background);
                            }
                        }
                        listener.onBackgroundListReceived(backgroundList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("TAG", "Catched : " + e.getMessage());

            }
        });
    }

    private static String downloadBackground(Context context, String backgroundImageUrl) {
        try {
            URL url = new URL(backgroundImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File backgroundDir = new File(context.getFilesDir(), "backgrounds");
            if (!backgroundDir.exists()) {
                backgroundDir.mkdirs();
            }

            String fileName = backgroundImageUrl.substring(backgroundImageUrl.lastIndexOf('/') + 1);
            File backgroundFile = new File(backgroundDir, fileName);
            FileOutputStream outputStream = new FileOutputStream(backgroundFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return backgroundFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnBackgroundListReceivedListener {
        void onBackgroundListReceived(List<Background> backgroundList);
    }
}
