package com.samset.imagedownloading_with_intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by samset on 23/05/16.
 */
public class IntentServiceDownloader extends IntentService {

    public static final int DOWNLOAD_ERROR = 10;
    public static final int DOWNLOAD_SUCCESS = 11;


    public IntentServiceDownloader() {
        super("Downloader");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        String url = intent.getStringExtra("url");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();


        File downloadFile = new File("/sdcard/sample.png");
        if (downloadFile.exists())

            downloadFile.delete();

        try {

            downloadFile.createNewFile();
            URL downloadURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setInstanceFollowRedirects(false);
            //conn.connect();
            int responseCode = conn.getResponseCode();

            Log.v("Service", " Code " + responseCode);

            if (responseCode != 200)

                throw new Exception("Error in connection");

            InputStream is = conn.getInputStream();

            FileOutputStream os = new FileOutputStream(downloadFile);

            byte buffer[] = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteCount);
            }
            os.close();
            is.close();

            SystemClock.sleep(10000);

            String filePath = downloadFile.getPath();
            bundle.putString("filePath", filePath);
            receiver.send(DOWNLOAD_SUCCESS, bundle);

        } catch (Exception e) {
            receiver.send(DOWNLOAD_ERROR, Bundle.EMPTY);
            e.printStackTrace();
        }
    }

}
