package com.samset.imagedownloading_with_intentservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView urlText;
    ProgressBar pd;
    ImageView imgView;
    Button button;
    SampleResultReceiver resultReceiever;
    String defaultUrl = "https://developer.android.com/assets/images/dac_logo.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultReceiever = new SampleResultReceiver(new Handler());
        urlText=(TextView)findViewById(R.id.tv_url);
        pd=(ProgressBar)findViewById(R.id.downloadPD);
        imgView=(ImageView)findViewById(R.id.imgView);
        button=(Button)findViewById(R.id.btn_download);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickService();
            }
        });




    }

 private void onClickService()
 {
     Intent startIntent = new Intent(MainActivity.this, IntentServiceDownloader.class);

         startIntent.putExtra("receiver", resultReceiever);
         startIntent.putExtra("url",defaultUrl);
         startService(startIntent);


     pd.setVisibility(View.VISIBLE);
     pd.setIndeterminate(true);
 }

    @SuppressLint("ParcelCreator")
    private class SampleResultReceiver extends ResultReceiver {

        public SampleResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                case IntentServiceDownloader.DOWNLOAD_ERROR:

                    Toast.makeText(getApplicationContext(), "error in download",
                            Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.INVISIBLE);
                    break;

                case IntentServiceDownloader.DOWNLOAD_SUCCESS:
                    String filePath = resultData.getString("filePath");
                    Bitmap bmp = BitmapFactory.decodeFile(filePath);
                    if (imgView != null && bmp != null) {
                    imgView.setImageBitmap(bmp);
                    Toast.makeText(getApplicationContext(),
                            "image download ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "error in decoding downloaded file",
                            Toast.LENGTH_SHORT).show();
                }
                pd.setIndeterminate(false);
                pd.setVisibility(View.INVISIBLE);

                break;
            }
            super.onReceiveResult(resultCode, resultData);
        }

    }
}
