package com.munchmash.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.munchmash.R;
import com.munchmash.model.FoodieCheckInModel;
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

/**
 * Created by sonu on 12/9/2017.
 */

public class FoodieAddPostActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CHECK_IN = 500;
    private Toolbar toolbar;
    private TextView toolbarTitle, postText, nameText;
    private LinearLayout addPhotoLayout;
    private UserModel userModel;
    private ImageView photoImage, profileImage;
    private EditText etMesssage;
    private Uri resultUri;
    private Spinner postTypeSpinner;
    private int postType = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_add_post);
        setUpToolbar();
    }

    @Override
    protected void initUI() {
        toolbar = findViewById(R.id.toolbar);
        addPhotoLayout = findViewById(R.id.ll_photo);
        photoImage = findViewById(R.id.photo_image_view);
        profileImage = findViewById(R.id.profile_image);
        etMesssage = findViewById(R.id.et_foodie_message);
        postText = findViewById(R.id.post_text);
        nameText = findViewById(R.id.tv_name);
        postTypeSpinner = findViewById(R.id.post_type_spinner);
    }

    @Override
    protected void initialiseListener() {
        addPhotoLayout.setOnClickListener(this);
        postText.setOnClickListener(this);
        userModel = SharedPreference.getUserModel(this);
        postTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        postType = 1;
                        break;
                    case 1:
                        postType = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.ll_check_in).setOnClickListener(this);
    }

    @Override
    protected void setData() {

        nameText.setText(userModel.getFirstName() + " " + userModel.getLastName());
        try {
            if (!TextUtils.isEmpty(userModel.getProfilePic()))
                Glide.with(this).load(userModel.getProfilePic()).into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = findViewById(R.id.tv_title);
        toolbarTitle.setText("Add post");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_photo:
                DialogUtils.showMediaDialog(this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(FoodieAddPostActivity.this);

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(FoodieAddPostActivity.this);

                    }
                });
                break;
            case R.id.post_text:
                if (isValid()) {
                    ImageUpload(resultUri.getPath(), userModel.getUserId(), etMesssage.getText().toString().trim());
                }
                break;
            case R.id.ll_check_in:
                startActivityForResult(new Intent(FoodieAddPostActivity.this, CheckInActivity.class), REQUEST_CHECK_IN);
                break;
        }
    }

    private boolean isValid() {
        String titleStr = etMesssage.getText().toString().trim();
        if (resultUri == null) {
            DialogUtils.showAlert(this, "Please select picture for the post.");
            return false;
        } else if (titleStr.isEmpty()) {
            DialogUtils.showAlert(this, "Please write description for the post.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri uri = Utils.getCameraUri();
                    System.out.println("Camera URI:-" + uri);
                    if (uri != null) {
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
                    System.out.println("Gallery URI:-" + uri);
                    if (uri != null) {
                        CropImage.activity(uri)
                                .start(this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    photoImage.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;
            case REQUEST_CHECK_IN:
                if (resultCode == RESULT_OK) {
                    FoodieCheckInModel foodieCheckInModel = (FoodieCheckInModel) data.getSerializableExtra(Constants.BundleKeys.CHECK_IN_MODEL);
                    System.out.println(foodieCheckInModel.getFirstName() + " " + foodieCheckInModel.getShopName() + " " + foodieCheckInModel.getAddress());
                    etMesssage.setText(etMesssage.getText().toString() + " Eating at " + foodieCheckInModel.getShopName());
                    etMesssage.setSelection(etMesssage.getText().length());
                }
                break;
        }
    }

    public void ImageUpload(String imagePath, String userId, String message) {
        System.out.println("Image Upload userId:-" + userId);
        try {

            String postUrl = Utils.parseUrl(Constants.uploadImageURL.FOOD_POST_UPLOAD_URL + postType + "&Title=&Tag=&Message=" + message + "&UserId=" + userId);
            File compressImageFile = new Compressor(this).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            UploadFileTask fileTask = new UploadFileTask(this, postUrl, compressImageFile.getPath(), multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("image url response " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("StatusCode").equals("100")) {
                            Toast.makeText(FoodieAddPostActivity.this, "Profile Image Updated Successfully", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        } else if (object.optString("StatusCode").equals("100")) {
                            DialogUtils.showAlert(FoodieAddPostActivity.this, object.optString("StatusMessage"));
                        }
                        String image_url = object.optString("Url");
                        System.out.println("image url  " + image_url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FoodieAddPostActivity.this, "Profile Image Not updated", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
