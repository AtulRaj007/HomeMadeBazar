package com.homemadebazar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.homemadebazar.R;
import com.homemadebazar.model.MarketPlaceProductBrandModel;
import com.homemadebazar.model.MarketPlaceProductCategoryModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.GetRequest;
import com.homemadebazar.network.UploadFileTask;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.JSONParsingUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;

public class MarketPlaceAddProductActivity extends BaseActivity implements View.OnClickListener {
    private EditText etProductName, etProductPrice, etProductDesc;
    private Spinner sprProductCategory, sprProductBrand;
    private ImageView ivProductImage;
    private Uri productImageUri;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_add_product);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(this);
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);
        etProductDesc = findViewById(R.id.et_product_desc);

        sprProductBrand = findViewById(R.id.spr_product_brand);
        sprProductCategory = findViewById(R.id.spr_product_category);

        ivProductImage = findViewById(R.id.iv_product_image);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.iv_product_image).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void setData() {
        getProductBrandListApiCall();
        getProductCategoryList();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Product Add");

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etProductName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter product name.");
            return false;
        } else if (TextUtils.isEmpty(etProductPrice.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter price.");
            return false;
        } else if (TextUtils.isEmpty(etProductDesc.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter description.");
            return false;
        } else if (productImageUri == null) {
            DialogUtils.showAlert(this, "Please select Product Image.");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (isValid()) {
                    MarketPlaceProductCategoryModel marketPlaceProductCategoryModel = (MarketPlaceProductCategoryModel) sprProductCategory.getItemAtPosition(sprProductCategory.getSelectedItemPosition());
                    MarketPlaceProductBrandModel marketPlaceProductBrandModel = (MarketPlaceProductBrandModel) sprProductBrand.getItemAtPosition(sprProductBrand.getSelectedItemPosition());
                    ImageUpload(productImageUri.getPath(), etProductName.getText().toString().trim(), marketPlaceProductCategoryModel.getProductCategoryId(), marketPlaceProductBrandModel.getBrandId(), etProductPrice.getText().toString().trim(),
                            etProductDesc.getText().toString().trim());
                }
                break;
            case R.id.btn_cancel:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.iv_product_image:
                DialogUtils.showMediaDialog(this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(MarketPlaceAddProductActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(MarketPlaceAddProductActivity.this);
                    }
                });
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
                        ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ivProductImage.setImageURI(uri);
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
                        ivProductImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ivProductImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    productImageUri = result.getUri();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getAddProductUrl(String productName, String categoryId, String brandId, String price, String description) {
        String url = Constants.uploadImageURL.MARKETPLACE_ADD_EDIT_PRODUCT + userModel.getUserId() + "&ProductName=" + productName + "&ProductCatId=" + categoryId +
                "&ProductBrandId=" + brandId + "&Price=" + price + "&Desc=" + description + "&OPerationMode=" + true + "&ProductId=" + "";
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }

    public void ImageUpload(String imagePath, String productName, String categoryId, String brandId, String price, String description) {
        String url = getAddProductUrl(productName, categoryId, brandId, price, description);
        try {
            File compressImageFile = new Compressor(this).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            final UploadFileTask fileTask = new UploadFileTask(this, url, compressImageFile.getPath(), multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("image url response " + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(MarketPlaceAddProductActivity.this, "Product added successfully.", new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MarketPlaceAddProductActivity.this, object.optString("StatusMessage"));
                        }
                        String image_url = object.optString("Url");
                        System.out.println("image url  " + image_url);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MarketPlaceAddProductActivity.this, "Profile Image Not updated", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBrandServiceUrl() {
        String url = Constants.ServerURL.MARKETPLACE_GET_PRODUCT_BRAND_LIST + userModel.getUserId();
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }

    private void getProductBrandListApiCall() {
        new GetRequest(this, new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println(Constants.ServiceTAG.RESPONSE + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                        ArrayList<MarketPlaceProductBrandModel> tempProductBrandModel = JSONParsingUtils.parseProductBrandModel(object);
                        ArrayAdapter<MarketPlaceProductBrandModel> spinnerAdapter = new ArrayAdapter<MarketPlaceProductBrandModel>(MarketPlaceAddProductActivity.this, android.R.layout.simple_list_item_1, tempProductBrandModel);
                        spinnerAdapter.setDropDownViewResource(R.layout
                                .simple_list_item);
                        sprProductBrand.setAdapter(spinnerAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).execute(getBrandServiceUrl());
    }

    private String getCategoryServiceUrl() {
        String url = Constants.ServerURL.MARKETPLACE_GET_PRODUCT_CATEGORY_LIST + userModel.getUserId();
        System.out.println(Constants.ServiceTAG.URL + url);
        return url;
    }

    private void getProductCategoryList() {
        new GetRequest(this, new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println(Constants.ServiceTAG.RESPONSE + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                        ArrayList<MarketPlaceProductCategoryModel> productCategoryModelArrayList = JSONParsingUtils.parseProductCategoryModel(object);
                        ArrayAdapter<MarketPlaceProductCategoryModel> spinnerAdapter = new ArrayAdapter<MarketPlaceProductCategoryModel>(MarketPlaceAddProductActivity.this, android.R.layout.simple_list_item_1, productCategoryModelArrayList);
                        spinnerAdapter.setDropDownViewResource(R.layout
                                .simple_list_item);
                        sprProductCategory.setAdapter(spinnerAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).execute(getCategoryServiceUrl());
    }
}
