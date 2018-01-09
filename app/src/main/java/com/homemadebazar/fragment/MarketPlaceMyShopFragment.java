package com.homemadebazar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.homemadebazar.R;
import com.homemadebazar.activity.MarketPlaceManageActivity;
import com.homemadebazar.activity.UpdateShopDetailsActivity;
import com.homemadebazar.adapter.ImagePagerAdapter;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.MultiPartRequest;
import com.homemadebazar.network.UploadFileTask;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.ShowHomeChefProfileApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by atulraj on 23/11/17.
 */

public class MarketPlaceMyShopFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout manageLayout, totalSaleLayout;
    private ImageView ivProfileImage, ivChangeProfilePhoto, ivEditShopDetails;
    private UserModel userModel;
    private RequestQueue mRequest;
    private MultiPartRequest mMultiPartRequest;
    private ArrayList<File> mFile = new ArrayList<File>();
    private ProgressBar mProgressBar;
    private ViewPager viewPager;
    private ArrayList<String> arrayList = new ArrayList<>();
    private CircleIndicator circleIndicator;
    private TextView tvShopName, tvPriceRange, tvAddress, tvSpeciality;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_myshop, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        manageLayout = getView().findViewById(R.id.manage_layout);
        totalSaleLayout = getView().findViewById(R.id.total_sale_today_layout);

        userModel = SharedPreference.getUserModel(getActivity());
        viewPager = getView().findViewById(R.id.view_pager);
        ivProfileImage = getView().findViewById(R.id.iv_profile);
        ivChangeProfilePhoto = getView().findViewById(R.id.iv_change_photo);
        ivEditShopDetails = getView().findViewById(R.id.iv_edit_shop_details);

        circleIndicator = getView().findViewById(R.id.indicator);

        tvShopName = getView().findViewById(R.id.tv_shop_name);
        tvPriceRange = getView().findViewById(R.id.tv_price_range);
        tvAddress = getView().findViewById(R.id.tv_address);
        tvSpeciality = getView().findViewById(R.id.tv_speciality);
    }

    @Override
    protected void initialiseListener() {
        ivChangeProfilePhoto.setOnClickListener(this);
        ivEditShopDetails.setOnClickListener(this);
        manageLayout.setOnClickListener(this);
        totalSaleLayout.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        getHomeChefProfileDetails();

    }

    public void getHomeChefProfileDetails() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final ShowHomeChefProfileApiCall apiCall = new ShowHomeChefProfileApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            HomeChefProfileModel homeChefProfileModel = apiCall.getResult();
                            if (homeChefProfileModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                try {
                                    tvShopName.setText(homeChefProfileModel.getShopName() + "");
                                    tvPriceRange.setText(homeChefProfileModel.getPriceRange() + "");
                                    tvAddress.setText(homeChefProfileModel.getAddress() + "");
                                    tvSpeciality.setText(homeChefProfileModel.getSpeciality() + "");
                                    if (!TextUtils.isEmpty(homeChefProfileModel.getProfilePicture()))
                                        Picasso.with(getActivity()).load(homeChefProfileModel.getProfilePicture()).into(ivProfileImage);
                                    ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getActivity(), homeChefProfileModel.getCoverPhotoUrl());
                                    viewPager.setAdapter(imagePagerAdapter);
                                    circleIndicator.setViewPager(viewPager);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                DialogUtils.showAlert(getActivity(), homeChefProfileModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), getActivity(), null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), getActivity(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = Utils.getCameraUri();
                    System.out.println("Camera URI:-" + uri);
                    if (uri != null) {
                        ivProfileImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(getContext(), this);
                    }

                } else {
                    DialogUtils.showAlert(getActivity(), "Camera Cancelled");
                }
                break;
            case Constants.Keys.REQUEST_GALLERY:
                if (resultCode == getActivity().RESULT_OK) {
                    System.out.println();
                    Uri uri = data.getData();
                    System.out.println("Gallary URI:-" + uri);
                    if (uri != null) {
                        ivProfileImage.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(getContext(), this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    ImageUpload(resultUri.getPath(), userModel.getUserId());
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;
            case Constants.Keys.UPDATE_SHOP_DETAILS:
                getHomeChefProfileDetails();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ImageUpload(String imagePath, String userId) {

        try {
            File compressImageFile = new Compressor(getActivity()).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            UploadFileTask fileTask = new UploadFileTask(getActivity(), Constants.uploadImageURL.PROFILE_IMAGE_UPLOAD + userId, compressImageFile.getPath(), multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("image url response " + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        if (object.optString("StatusCode").equals(Constants.ServerResponseCode.SUCCESS)) {
                            DialogUtils.showAlert(getActivity(), "Profile Image Updated Successfully");
                        } else {
                            DialogUtils.showAlert(getActivity(), object.optString("StatusMessage"));
                        }
                        String image_url = object.optString("Url");
                        System.out.println("image url  " + image_url);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Profile Image Not updated", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_layout:
                Intent intent = new Intent(getActivity(), MarketPlaceManageActivity.class);
                startActivity(intent);
                break;
            case R.id.total_sale_today_layout:
                break;

            case R.id.iv_change_photo:
                DialogUtils.showMediaDialog(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(getActivity(), MarketPlaceMyShopFragment.this);

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(getActivity(), MarketPlaceMyShopFragment.this);

                    }
                });
                break;
            case R.id.iv_edit_shop_details:
                Intent editShopIntent = new Intent(getActivity(), UpdateShopDetailsActivity.class);
                startActivityForResult(editShopIntent, Constants.Keys.UPDATE_SHOP_DETAILS);
                break;
        }
    }
}
