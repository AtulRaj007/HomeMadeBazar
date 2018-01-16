package com.homemadebazar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.activity.CreateOrderActivity;
import com.homemadebazar.activity.UpdateShopDetailsActivity;
import com.homemadebazar.adapter.ImagePagerAdapter;
import com.homemadebazar.adapter.MyShopAdapter;
import com.homemadebazar.model.HomeChefOrderModel;
import com.homemadebazar.model.HomeChefProfileModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.MultiPartRequest;
import com.homemadebazar.network.UploadFileTask;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.GetHomeChefOrderListApiCall;
import com.homemadebazar.network.apicall.ShowHomeChefProfileApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by HP on 7/29/2017.
 */

public class MyShopFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private MyShopAdapter myShopAdapter;
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
    private ArrayList<HomeChefOrderModel> homeChefOrderModelArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_shop, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initialiseListener() {

        ivChangeProfilePhoto.setOnClickListener(this);
        ivEditShopDetails.setOnClickListener(this);

        getView().findViewById(R.id.tv_create_order).setOnClickListener(this);
        getView().findViewById(R.id.tv_invite_friends).setOnClickListener(this);
    }

    public void initUI() {

        userModel = SharedPreference.getUserModel(getActivity());
        viewPager = getView().findViewById(R.id.view_pager);
        recyclerView = getView().findViewById(R.id.recycler_view);
        ivProfileImage = getView().findViewById(R.id.iv_profile);
        ivChangeProfilePhoto = getView().findViewById(R.id.iv_change_photo);
        ivEditShopDetails = getView().findViewById(R.id.iv_edit_shop_details);

        circleIndicator = getView().findViewById(R.id.indicator);

        tvShopName = getView().findViewById(R.id.tv_shop_name);
        tvPriceRange = getView().findViewById(R.id.tv_price_range);
        tvAddress = getView().findViewById(R.id.tv_address);
        tvSpeciality = getView().findViewById(R.id.tv_speciality);

    }

    public void setData() {
        myShopAdapter = new MyShopAdapter(getActivity(), homeChefOrderModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myShopAdapter);
        getHomeChefProfileDetails();
        getOrderDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create_order:
                startActivityForResult(new Intent(getActivity(), CreateOrderActivity.class), Constants.Keys.CREATE_ORDER);
                break;

            case R.id.tv_invite_friends:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Sharing to Social Platform");
                startActivity(intent);
                break;

            case R.id.iv_change_photo:
                DialogUtils.showMediaDialog(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(getActivity(), MyShopFragment.this);

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(getActivity(), MyShopFragment.this);

                    }
                });
                break;

            case R.id.iv_edit_shop_details:
                Intent editShopIntent = new Intent(getActivity(), UpdateShopDetailsActivity.class);
                startActivityForResult(editShopIntent, Constants.Keys.UPDATE_SHOP_DETAILS);
                break;
        }
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
            case Constants.Keys.CREATE_ORDER:
                getOrderDetails();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.cameraIntent(getActivity());
                }
                break;
            case Constants.Keys.REQUEST_PERMISSION_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.gallaryIntent(getActivity());
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                                        Glide.with(getActivity()).load(homeChefProfileModel.getProfilePicture()).into(ivProfileImage);
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

    public void getOrderDetails() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final GetHomeChefOrderListApiCall apiCall = new GetHomeChefOrderListApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            ArrayList<HomeChefOrderModel> tempHomeChefOrderModelArrayList = apiCall.getResult();
                            homeChefOrderModelArrayList.clear();
                            homeChefOrderModelArrayList.addAll(tempHomeChefOrderModelArrayList);
                            Collections.reverse(homeChefOrderModelArrayList);
                            myShopAdapter.notifyDataSetChanged();
                            System.out.println(">>>>>> ");
                            //                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
//                                Log.d(TAG, userModel.toString());
//                                uploadFile(userModel.getUserId());
//                            } else {
//                                DialogUtils.showAlert(getActivity(), userModel.getStatusMessage());
//                            }

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

}
