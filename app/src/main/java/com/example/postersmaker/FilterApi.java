package com.example.postersmaker;

import android.content.Context;
import android.util.Log;

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

public class FilterApi {

    private static final String BASE_URL = "https://poster-maker.sftester.pw/api/V1/filters";

    public static void getFilterListFromApi(final Context context, final OnFilterListReceivedListener listener) {
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
                        List<Filter> filterList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject filterObject = jsonArray.getJSONObject(i);
                            String filterImageUrl = filterObject.getString("image_path");
                            Filter filter = new Filter();
                            String filePath = downloadFilter(context, filterImageUrl);
                            if (filePath != null) {
                                filter.setName("Filter " + (i + 1));
                                filter.setFilePath(filePath);
                                filterList.add(filter);
                                Log.d("TAG", "onResponse: "+ filterList.size());
                            }

                        }

                        listener.onFilterListReceived(filterList);
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

    private static String downloadFilter(Context context, String filterImageUrl) {
        try {
            URL url = new URL(filterImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File filterDir = new File(context.getFilesDir(), "filters");
            if (!filterDir.exists()) {
                filterDir.mkdirs();
            }

            String fileName = filterImageUrl.substring(filterImageUrl.lastIndexOf('/') + 1);
            File filterFile = new File(filterDir, fileName);
            FileOutputStream outputStream = new FileOutputStream(filterFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return filterFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnFilterListReceivedListener {
        void onFilterListReceived(List<Filter> filterList);
    }
}
