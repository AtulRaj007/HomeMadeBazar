package com.homemadebazar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.adapter.ProfileRecyclerAdapter;
import com.homemadebazar.model.ProfileInterestsModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.UploadFileTask;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import id.zelory.compressor.Compressor;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private EditText etFirstName, etLastName, etEmailId, etPhoneNumber, etCountry, etAboutYourSelf, etProfession;
    private Spinner sprProfession;
    private TextView tvInterestsSelected;
    private String selectedInterests;
    private ImageView ivProfilePic;
    private RecyclerView recyclerView;
    private Uri profilePicUri = null;
    private LinearLayoutManager linearLayoutManager;
    private ProfileRecyclerAdapter profileRecyclerAdapter;
    private ArrayList<ProfileInterestsModel> profileInterestsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setupToolbar();
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("My Profile");
    }

    @Override
    protected void initUI() {
        linearLayoutManager = new LinearLayoutManager(MyProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        userModel = SharedPreference.getUserModel(MyProfileActivity.this);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmailId = findViewById(R.id.et_email_id);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etCountry = findViewById(R.id.et_country);
        etAboutYourSelf = findViewById(R.id.et_about_your_self);
        sprProfession = findViewById(R.id.spr_profession);
        etProfession = findViewById(R.id.et_profession);
//        etCompanyName = findViewById(R.id.et_company_name);
//        etUniversityName = findViewById(R.id.et_university_name);
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        recyclerView = findViewById(R.id.recycler_view);
        tvInterestsSelected = findViewById(R.id.tv_interest_selected);

        if (userModel != null) {
            etFirstName.setText(userModel.getFirstName());
            etLastName.setText(userModel.getLastName());
            etEmailId.setText(userModel.getEmailId());
            etPhoneNumber.setText(userModel.getMobile());
            etCountry.setText(userModel.getCountryName());
            if (!TextUtils.isEmpty(userModel.getProfilePic())) {
                Glide.with(MyProfileActivity.this).load(userModel.getProfilePic()).into(ivProfilePic);
            }
        }
    }

    private String getSelectedInterests() {
        selectedInterests = "";
        for (int i = 0; i < profileInterestsModelArrayList.size(); i++) {
            if (profileInterestsModelArrayList.get(i).isSelected()) {
                selectedInterests = selectedInterests + "1;";
            } else {
                selectedInterests = selectedInterests + "0;";
            }
        }
        System.out.println("Interests Selected " + "\n" + selectedInterests);
        return selectedInterests;
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_update_profile).setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);
        sprProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    findViewById(R.id.til_profession).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.til_profession).setVisibility(View.VISIBLE);
                    if (position == 1) {//STUDENT
                        etProfession.setHint(getResources().getString(R.string.hint_student));
                    } else if (position == 2) {//PROFESSIONAL
                        etProfession.setHint(getResources().getString(R.string.hint_professional));
                    } else if (position == 3) {//SELF-EMPLOYED
                        etProfession.setHint(getResources().getString(R.string.hint_self_employed));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void setData() {
        initialiseProfileInterests();
        recyclerView.setLayoutManager(linearLayoutManager);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(this, profileInterestsModelArrayList, true);
        recyclerView.setAdapter(profileRecyclerAdapter);
    }

    private void initialiseProfileInterests() {
        profileInterestsModelArrayList.clear();
        for (int i = 0; i < Constants.profileInterests.length; i++) {
            ProfileInterestsModel profileInterestsModel = new ProfileInterestsModel();
            try {
                profileInterestsModel.setIconId(Integer.parseInt(Constants.profileInterests[i][0]));
                profileInterestsModel.setInterestName(Constants.profileInterests[i][1]);
                profileInterestsModel.setSelected(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            profileInterestsModelArrayList.add(profileInterestsModel);
        }
    }

    private boolean isValid() {
        getSelectedInterests();
        if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter first name.");
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter last name.");
            return false;
        } else if (TextUtils.isEmpty(etEmailId.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter emailId.");
            return false;
        } else if (TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter Mobile number");
            return false;
        } else if (TextUtils.isEmpty(etCountry.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter country name");
            return false;
        } else if (TextUtils.isEmpty(etAboutYourSelf.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter about your self.");
            return false;
        }
        /*else if (TextUtils.isEmpty(etAadharNumber.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter aadhar number");
            return false;
        }*/
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_profile:
                if (isValid()) {
                    updateUserProfile(profilePicUri.getPath());
                }
                break;
            case R.id.iv_profile_pic:
                DialogUtils.showMediaDialog(this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(MyProfileActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(MyProfileActivity.this);
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
//                        ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        ivProfilePic.setImageURI(uri);
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
                        ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ivProfilePic.setImageURI(uri);
                        CropImage.activity(uri)
                                .start(this);
                    }

                }

                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    profilePicUri = result.getUri();
                    ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ivProfilePic.setImageURI(profilePicUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    http://localhost:14013/api/Miscellaneous/ProfileUpdate?FName=ABC&LName=Shah&Email=edu.panks@gmail.com&Country=india&DPStatus=failure is the key of success&Mobile=2222222222&UserId=1712265"
    private String getProfileUpdateUrl() {
        try {
            String url = Constants.ServerURL.PROFILE_UPDATE + "?UserId=" + userModel.getUserId() + "&FName=" + etFirstName.getText().toString() + "&LName=" + etLastName.getText().toString() + "&Email=" + etEmailId.getText().toString() + "&Country=" + etCountry.getText().toString() +
                    "&DPStatus=" + etAboutYourSelf.getText().toString() + "&Mobile=" + etPhoneNumber.getText().toString() + "&CompanyName=" + "" + "&UniversityName=" + "";
            System.out.println(Constants.ServiceTAG.URL + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public void updateUserProfile(String imagePath) {
        String url = getProfileUpdateUrl();
        try {
            File compressImageFile = null;
            if (!TextUtils.isEmpty(imagePath))
                compressImageFile = new Compressor(this).compressToFile(new File(imagePath));

            Hashtable<String, String> multipartParams = new Hashtable<>();

            final UploadFileTask fileTask = new UploadFileTask(this, url, compressImageFile != null ? compressImageFile.getPath() : "", multipartParams, "image_url", new UploadFileTask.FileUploadListener() {
                @Override
                public void onComplete(String response) {
                    System.out.println("image url response " + response);
                    try {

                        JSONObject object = new JSONObject(response);
                        if (object.optInt("StatusCode") == Constants.ServerResponseCode.SUCCESS) {
                            String firstName = object.optString("FName");
                            String lastName = object.optString("FName");
                            String emailId = object.optString("Email");
                            String country = object.optString("Country");
                            String mobileNumber = object.optString("Mobile");
                            String dpStatus = object.optString("DPStatus");
                            String profileUrl = object.optString("Url");
                            String companyName = object.optString("CompanyName");
                            String universityName = object.optString("UniversityName");

                            userModel.setProfilePic(profileUrl);
                            SharedPreference.saveUserModel(MyProfileActivity.this, userModel);

                            DialogUtils.showAlert(MyProfileActivity.this, "Profile updated successfully.", new Runnable() {
                                @Override
                                public void run() {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(MyProfileActivity.this, object.optString("StatusMessage"));
                        }
                        String image_url = object.optString("Url");
                        System.out.println("image url  " + image_url);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MyProfileActivity.this, "Profile Image Not updated", Toast.LENGTH_LONG).show();
                    }

                }
            });
            fileTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

