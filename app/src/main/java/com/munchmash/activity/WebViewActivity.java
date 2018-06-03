package com.munchmash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.munchmash.R;


/**
 * Created by and-04 on 20/11/17.
 */

public class WebViewActivity extends BaseActivity {
    private TextView toolbarTitle;
    private WebView webView;
    private String title, url;
    private static String KEY_TITLE = "KEY_TITLE";
    private static String KEY_URL = "KEY_URL";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setUpToolbar();
    }

    public static Intent getWebViewIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    public void initUI() {
        getDataFromBundle();
        webView = findViewById(R.id.web_view);
    }

    private void getDataFromBundle() {
        title = getIntent().getStringExtra(KEY_TITLE);
        url = getIntent().getStringExtra(KEY_URL);
    }

    @Override
    public void initialiseListener() {
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void setData() {
        webView.loadUrl(url);
    }

    private void setUpToolbar() {
        toolbarTitle = findViewById(R.id.tv_title);
        toolbarTitle.setText(title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
