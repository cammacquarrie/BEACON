package com.comp3004.beacon.GUI;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.comp3004.beacon.R;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;
    Handler  mHandler;
    public static final String IMAGE_USER_ID = "USER_ID";
    String userId;
    ProgressBar imageProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_image_view);
        mHandler = new Handler();
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            userId = extras.getString(IMAGE_USER_ID);
        }


        loadPhotoToView();
    }

    public void loadPhotoToView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (PhotoSenderHandler.getInstance().getFile(userId) == null) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageProgressBar.setVisibility(View.GONE);
                        imageView.setImageDrawable(Drawable.createFromPath(PhotoSenderHandler.getInstance().getFile(userId).getPath()));
                    }
                });

            }
        }).start();
    }
}