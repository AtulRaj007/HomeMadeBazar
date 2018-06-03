package com.munchmash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.munchmash.R;
import com.munchmash.util.DownloadFile;


public class ImageViewActivity extends BaseActivity implements View.OnClickListener {

    private String imageUrl;
    private static String KEY_IMAGE = "KEY_IMAGE";


    @Override
    protected void initUI() {
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.iv_image_download).setOnClickListener(this);

    }

    @Override
    protected void setData() {

    }

    public static Intent getImageIntent(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra(KEY_IMAGE, imageUrl);
        return intent;
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Preview");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        setupToolbar();
        Intent intent = getIntent();
        if (null != intent) {
            imageUrl = intent.getStringExtra(KEY_IMAGE);
        }
        System.out.println("Image Url:-" + imageUrl);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(this).load(imageUrl).into(imageView);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image_download:
                if (!TextUtils.isEmpty(imageUrl))
                    new DownloadFile(ImageViewActivity.this).execute(imageUrl);
                break;
        }
    }
}