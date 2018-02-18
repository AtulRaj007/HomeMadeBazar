package com.homemadebazar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.homemadebazar.R;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class AcceptMoneyActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_STORAGE_PERMISSION = 105;
    private UserModel userModel;
    private TextView tvMobileNumber;
    private ImageView ivShare;
    private LinearLayout llRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_money);
        setupToolbar();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Accept Money");

    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(AcceptMoneyActivity.this);
        tvMobileNumber = findViewById(R.id.tv_mobile_number);
        ivShare = findViewById(R.id.iv_share);
        llRootLayout = findViewById(R.id.ll_root_layout);
    }

    @Override
    protected void initialiseListener() {
        ivShare.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        tvMobileNumber.setText(userModel.getMobile());
        try {
            //setting size of qr code
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;

            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            createQRCode(userModel.getMobile(), charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {

        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view
            ImageView myImage = findViewById(R.id.iv_qr_code);
            myImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    Bitmap screenShotBitmap = Utils.takeScreenshot(llRootLayout);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "download this image");
                    String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), screenShotBitmap, "title", null);
                    Uri bitmapUri = Uri.parse(bitmapPath);
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share QR via..."));
                }

                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.length > 0) {
            Bitmap screenShotBitmap = Utils.takeScreenshot(llRootLayout);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "download this image");
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), screenShotBitmap, "title", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share QR via..."));
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
