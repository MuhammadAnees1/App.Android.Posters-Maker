package com.example.postersmaker;

import android.content.Context;

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

public class FrameApi {

    private static final String BASE_URL = "https://poster-maker.sftester.pw/api/V1/frames";

    public static void getFrameListFromApi(final Context context, final OnFrameListReceivedListener listener) {
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
                        JSONArray itemsArray = jsonResponse.getJSONArray("data");
                        List<Frame> frameList = new ArrayList<>();
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject frameObject = itemsArray.getJSONObject(i);
                            String frameImageUrl = frameObject.getString("image_path");
                            Frame frame = new Frame();
                            String filePath = downloadFrame(context, frameImageUrl);
                            if (filePath != null) {
                                frame.setName("Frame " + (i + 1));
                                frame.setFilePath(filePath);
                                frameList.add(frame);
                            }

                        }

                        listener.onFrameListReceived(frameList);
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

    private static String downloadFrame(Context context, String frameImageUrl) {
        try {
            URL url = new URL(frameImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File frameDir = new File(context.getFilesDir(), "frames");
            if (!frameDir.exists()) {
                frameDir.mkdirs();
            }

            String fileName = frameImageUrl.substring(frameImageUrl.lastIndexOf('/') + 1);
            File frameFile = new File(frameDir, fileName);
            FileOutputStream outputStream = new FileOutputStream(frameFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return frameFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnFrameListReceivedListener {
        void onFrameListReceived(List<Frame> frameList);
    }
}
