package com.munchmash.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.munchmash.R;
import com.munchmash.model.UserModel;
import com.munchmash.network.UploadFileTask;
import com.munchmash.util.Constants;
import com.munchmash.util.DialogUtils;
import com.munchmash.util.SharedPreference;
import com.munchmash.util.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;

public class MarketPlaceAddCategoryActivity extends BaseActivity implements View.OnClickListener {
    private EditText etCategoryName, etDescription;
    private ImageView ivCategoryImage;
    private UserModel userModel;
    private Uri categoryImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_add_category);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(MarketPlaceAddCategoryActivity.this);
        etCategoryName = findViewById(R.id.et_category_name);
        etDescription = findViewById(R.id.et_description);
        ivCategoryImage = findViewById(R.id.iv_category_image);
    }

    @Override
    protected void initialiseListener() {
        ivCategoryImage.setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etCategoryName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter category name.");
            etCategoryName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etDescription.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter description.");
            etDescription.requestFocus();
            return false;
        } else if (categoryImageUri == null) {
            DialogUtils.showAlert(this, "Please select category image.");
            return false;
        }
        return true;
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Add Category");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_category_image:
                DialogUtils.showMediaDialog(this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(MarketPlaceAddCategoryActivity.this);

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(MarketPlaceAddCategoryActivity.this);

                    }
                });
                break;
            case R.id.btn_add:
                if (isValid()) {
                    ImageUpload(categoryImageUri.getPath(), etCategoryName.getText().toString(), etDescription.getText().toString());
                }
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = Utils.getCameraUri();
                    System.out.println("Camera URI:-" + uri);
                    if (uri != null) {
                        ivCategoryImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ivCategoryImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                } else {
                    DialogUtils.showAlert(this, "Camera Cancelled");
                }
                break;
            case Constants.Keys.REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    System.out.println();
                    Uri uri = data.getData();
                    System.out.println("Gallary URI:-" + uri);
                    if (uri != null) {
                        ivCategoryImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ivCategoryImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    categoryImageUri = result.getUri();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImageUrl(String name, String description) {
        String url = Constants.uploadImageURL.MARKETPLACE_ADD_CATEGORY + userModel.getUserId() + "&Name=" + name + "&Description=" + description;
        url = Utils.parseUrl(url);
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }

    public void ImageUpload(String imagePath, String name, String description) {
        String url = getImageUrl(name, description);
        try {
            File compressImageFile = new Compressor(this).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            UploadFileTask fileTask = new UploadFileTask(this, url, compressImageFile.getPath(), multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("image url response " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(MarketPlaceAddCategoryActivity.this, "Category added Successfully", new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MarketPlaceAddCategoryActivity.this, object.optString("StatusMessage"));
                        }
                        String image_url = object.optString("Url");
                        System.out.println("image url  " + image_url);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MarketPlaceAddCategoryActivity.this, "Error in adding category.", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
