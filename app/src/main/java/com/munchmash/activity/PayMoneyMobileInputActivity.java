package com.munchmash.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;
import com.munchmash.R;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by atulraj on 30/12/17.
 */

public class PayMoneyMobileInputActivity extends BaseActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_input_pay_money);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        scannerView = findViewById(R.id.scannar_view);

    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.et_enter_mobile).setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.Keys.REQUEST_CAMERA);
        } else {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        System.out.println("Scanned Text" + result.getText());
        System.out.println("Format:-" + result.getBarcodeFormat().toString());

        DialogUtils.showScanDialog(PayMoneyMobileInputActivity.this, result.getText(), new Runnable() {
            @Override
            public void run() {
                // Proceed
                startActivity(PayMoneyActivity.getPayIntent(PayMoneyMobileInputActivity.this, result.getText()));
                finish();

            }
        }, new Runnable() {
            @Override
            public void run() {
                // Cancel
                scannerView.resumeCameraPreview(PayMoneyMobileInputActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_enter_mobile:
                startActivity(PayMoneyActivity.getPayIntent(PayMoneyMobileInputActivity.this, ""));
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.Keys.REQUEST_CAMERA && grantResults.length > 0) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Pay Money");

    }
}
