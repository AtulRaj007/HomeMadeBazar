package com.homemadebazar.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.homemadebazar.R;
import com.homemadebazar.Template.Template;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.MultiPartRequest;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MyShopAddDetailsApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class UpdateShopDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ">>>>>UpdateShopDetails";
    private ImageView ivFirstCoverPhoto, ivSecondCoverPhoto, ivThirdCoverPhoto, ivFourthCoverPhoto, ivFifthCoverPhoto;
    private EditText etShopName, etPriceRange, etShopAddress, etFoodSpeciality;
    private Button btnSave;
    private int imageSelectedIndex = 0;
    private String coverPhotoArray[] = new String[5];
    private RequestQueue mRequest;
    private MultiPartRequest mMultiPartRequest;
    private ArrayList<File> mFile = new ArrayList<File>();
    private UserModel userModel;
    private Spinner sprMinPrice, sprMaxPrice;
    private HomeChefProfileModel profileModel;

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";

        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop_details);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        mRequest = Volley.newRequestQueue(UpdateShopDetailsActivity.this);
        userModel = SharedPreference.getUserModel(UpdateShopDetailsActivity.this);
        profileModel = SharedPreference.getProfileModel(UpdateShopDetailsActivity.this);

        ivFirstCoverPhoto = findViewById(R.id.iv_first_cover_photo);
        ivSecondCoverPhoto = findViewById(R.id.iv_second_cover_photo);
        ivThirdCoverPhoto = findViewById(R.id.iv_third_cover_photo);
        ivFourthCoverPhoto = findViewById(R.id.iv_fourth_cover_photo);
        ivFifthCoverPhoto = findViewById(R.id.iv_fifth_cover_photo);

        etShopName = findViewById(R.id.et_shop_name);
        etShopAddress = findViewById(R.id.et_shop_address);
        etFoodSpeciality = findViewById(R.id.et_food_speciality);

        sprMinPrice = findViewById(R.id.spr_min_price);
        sprMaxPrice = findViewById(R.id.spr_max_price);

        btnSave = findViewById(R.id.btn_save);

    }

    @Override
    protected void initialiseListener() {
        ivFirstCoverPhoto.setOnClickListener(this);
        ivSecondCoverPhoto.setOnClickListener(this);
        ivThirdCoverPhoto.setOnClickListener(this);
        ivFourthCoverPhoto.setOnClickListener(this);
        ivFifthCoverPhoto.setOnClickListener(this);

        btnSave.setOnClickListener(this);

    }

    @Override
    protected void setData() {

        ArrayList<String> minPriceArrayList = Utils.getPriceArray("Min Price");
        ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, minPriceArrayList);
        sprMinPrice.setAdapter(minAdapter);

        ArrayList<String> maxPriceArrayList = Utils.getPriceArray("Max Price");
        ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, maxPriceArrayList);
        sprMaxPrice.setAdapter(maxAdapter);

        if (profileModel != null) {
            try {
                etShopName.setText(profileModel.getShopName());
                etShopAddress.setText(profileModel.getAddress());
                etFoodSpeciality.setText(profileModel.getSpeciality());
                String priceRange = profileModel.getPriceRange();
                if (!TextUtils.isEmpty(priceRange)) {
                    String rangesArray[] = priceRange.split("-");
                    if (rangesArray.length == 2) {
                        sprMinPrice.setSelection((Integer.parseInt(rangesArray[0]) / 20));
                        sprMaxPrice.setSelection((Integer.parseInt(rangesArray[1]) / 20));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.tv_title)).setText("Update Shop Details");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_first_cover_photo:
                imageSelectedIndex = 0;
                DialogUtils.showMediaDialog(UpdateShopDetailsActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(UpdateShopDetailsActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                    }
                });

                break;
            case R.id.iv_second_cover_photo:
                imageSelectedIndex = 1;
                DialogUtils.showMediaDialog(UpdateShopDetailsActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(UpdateShopDetailsActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                    }
                });

                break;
            case R.id.iv_third_cover_photo:
                imageSelectedIndex = 2;
                DialogUtils.showMediaDialog(UpdateShopDetailsActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(UpdateShopDetailsActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                    }
                });

                break;
            case R.id.iv_fourth_cover_photo:
                imageSelectedIndex = 3;
                DialogUtils.showMediaDialog(UpdateShopDetailsActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(UpdateShopDetailsActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                    }
                });


                break;
            case R.id.iv_fifth_cover_photo:
                imageSelectedIndex = 4;

                DialogUtils.showMediaDialog(UpdateShopDetailsActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(UpdateShopDetailsActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                    }
                });


                break;
            case R.id.btn_save:
                if (isValid())
                    addShopDetails();
                break;
        }

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etShopName.getText().toString().trim())) {
            DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Please enter shop name.");
            return false;
        }
        /*else if (TextUtils.isEmpty(etPriceRange.getText().toString().trim())) {
            DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Please enter price range.");
            return false;
        }*/
        else if (TextUtils.isEmpty(etShopAddress.getText().toString().trim())) {
            DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Please enter address.");
            return false;
        } else if (TextUtils.isEmpty(etFoodSpeciality.getText().toString().trim())) {
            DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Please enter food speciality.");
            return false;
        }
    /*    else if (mFile.size() == 0) {
            DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Please select cover photo.");
            return false;
        }*/
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.cameraIntent(UpdateShopDetailsActivity.this);
                }
                break;
            case Constants.Keys.REQUEST_PERMISSION_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.gallaryIntent(UpdateShopDetailsActivity.this);
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setImage(Uri uri) {
        try {

            mFile.add(new File(getPath(UpdateShopDetailsActivity.this, uri)));
            switch (imageSelectedIndex) {
                case 0:
                    ivFirstCoverPhoto.setImageURI(uri);
                    coverPhotoArray[0] = uri.getPath();
                    break;
                case 1:
                    ivSecondCoverPhoto.setImageURI(uri);
                    coverPhotoArray[1] = uri.getPath();
                    break;
                case 2:
                    ivThirdCoverPhoto.setImageURI(uri);
                    coverPhotoArray[2] = uri.getPath();
                    break;
                case 3:
                    ivFourthCoverPhoto.setImageURI(uri);
                    coverPhotoArray[3] = uri.getPath();
                    break;
                case 4:
                    ivFifthCoverPhoto.setImageURI(uri);
                    coverPhotoArray[4] = uri.getPath();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    setImage(Utils.getCameraUri());
                } else {
                    DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Camera Cancelled");
                }
                break;
            case Constants.Keys.REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    System.out.println();
                    Uri uri = data.getData();
                    setImage(uri);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void uploadFile(String userId) {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateShopDetailsActivity.this);
        progressDialog.setMessage("Uploading File ... Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mRequest.start();
        mMultiPartRequest = new MultiPartRequest(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Shop Details update failed.", new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                progressDialog.hide();
                DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Shop Details updated Successfully.", new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        }, mFile, mFile.size(), Constants.uploadImageURL.COVER_PHOTO_IMAGE_UPLOAD + userId);

        mMultiPartRequest.setTag("MultiRequest");
        mMultiPartRequest.setRetryPolicy(new DefaultRetryPolicy(Template.VolleyRetryPolicy.SOCKET_TIMEOUT,
                Template.VolleyRetryPolicy.RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequest.add(mMultiPartRequest);
    }

    public void addShopDetails() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final MyShopAddDetailsApiCall apiCall = new MyShopAddDetailsApiCall(userModel.getUserId(), etShopName.getText().toString().trim(),
                    "", etShopAddress.getText().toString().trim(), etFoodSpeciality.getText().toString().trim());
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                Log.d(TAG, userModel.toString());
                                if (mFile.size() > 0) {
                                    uploadFile(userModel.getUserId());
                                } else {
                                    progressDialog.hide();
                                    DialogUtils.showAlert(UpdateShopDetailsActivity.this, "Shop Details updated Successfully.", new Runnable() {
                                        @Override
                                        public void run() {
                                            setResult(RESULT_OK);
                                            finish();
                                        }
                                    });
                                }
                            } else {
                                DialogUtils.showAlert(UpdateShopDetailsActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), UpdateShopDetailsActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), UpdateShopDetailsActivity.this, null);
        }
    }
}
