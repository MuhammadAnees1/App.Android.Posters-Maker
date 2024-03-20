package com.example.postersmaker;

import android.content.Context;
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

public class FontApi {
    private static final String BASE_URL = "https://www.googleapis.com/webfonts/v1/webfonts?key=AIzaSyDYXP-OyytlcSNj1jzgZZGC96WWwcP8zAs";

    public static void getFontListFromApi(final Context context,final OnFontListReceivedListener listener) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray itemsArray = jsonResponse.getJSONArray("items");
                        List<Font> fontList = new ArrayList<>();
                        for (int i = 0; i < 50; i++) {
                            JSONObject fontObject = itemsArray.getJSONObject(i);
                            JSONObject filesObject = fontObject.getJSONObject("files");

                                String fontUrl = filesObject.getString("regular");
                                Font font = new Font();
                            String filePath = downloadFont(context, fontUrl);
                            if (filePath != null) {
                                font.setName("Font " + (i + 1));
                                font.setFilePath(filePath);
                                fontList.add(font);
                            }

                        }

                        listener.onFontListReceived(fontList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static String downloadFont(Context context, String fontUrl) {
        try {
            URL url = new URL(fontUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File fontDir = new File(context.getFilesDir(), "fonts");
            if (!fontDir.exists()) {
                fontDir.mkdirs();
            }

            File fontFile = new File(fontDir, fontUrl.substring(fontUrl.lastIndexOf('/') + 1));
            FileOutputStream outputStream = new FileOutputStream(fontFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return fontFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnFontListReceivedListener {
        void onFontListReceived(List<Font> fontList);
    }
}
