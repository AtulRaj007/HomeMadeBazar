package com.homemadebazar.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieAddPostActivity;
import com.homemadebazar.adapter.FoodieFlashPostAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.FoodieFlashPostModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieGetPostsApiCall;
import com.homemadebazar.network.apicall.GetContactSyncApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieFlashFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CompoundButton.OnCheckedChangeListener {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Switch switchBtn;
    private FoodieFlashPostAdapter adapter;
    private UserModel userModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<FoodieFlashPostModel> foodieFlashPostModelArrayList = new ArrayList<>();
    private Cursor cursor;
    private Switch switchSyncContact;
    private boolean isApiRunning = false;
    private Button btnInviteFriends;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_flash, container, false);
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(getActivity());
        fab = getView().findViewById(R.id.fab);
        recyclerView = getView().findViewById(R.id.recycler_view);
        switchBtn = getView().findViewById(R.id.switch_pets_allowed);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
        switchSyncContact = getView().findViewById(R.id.switch_sync_contact);
        btnInviteFriends = getView().findViewById(R.id.btn_invite_friends);
    }

    @Override
    protected void initialiseListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(getActivity(), FoodieAddPostActivity.class);
                startActivityForResult(postIntent, Constants.Keys.FOODIE_POST);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        switchSyncContact.setOnCheckedChangeListener(this);
        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Utils.getAppStoreUrl(getActivity()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setData() {
        adapter = new FoodieFlashPostAdapter(getActivity(), userModel.getUserId(), foodieFlashPostModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getFoodieFlashPosts();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isFlashDataChanges) {
            Constants.isFlashDataChanges = false;
            getFoodieFlashPosts();
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    public void getFoodieFlashPosts() {
        try {
            final FoodieGetPostsApiCall apiCall = new FoodieGetPostsApiCall(userModel.getUserId());
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getBaseModel();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                ArrayList<FoodieFlashPostModel> tempFoodieFlashPostArrayList = apiCall.getResult();
                                foodieFlashPostModelArrayList.clear();
                                foodieFlashPostModelArrayList.addAll(tempFoodieFlashPostArrayList);
                                adapter.notifyDataSetChanged();
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.GONE);
                            } else if (baseModel.getStatusCode() == Constants.ServerResponseCode.NO_RECORD_FOUND) {
                                getView().findViewById(R.id.tv_no_record_found).setVisibility(View.VISIBLE);
                                foodieFlashPostModelArrayList.clear();
                                adapter.notifyDataSetChanged();
                            } else {
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
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
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constants.Keys.FOODIE_POST:
                    getFoodieFlashPosts();
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkReadContactPermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, Constants.Keys.READ_CONTACTS_REQUEST);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, Constants.Keys.READ_CONTACTS_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void getContactsIntoArrayList() {
        if (checkReadContactPermission(getActivity())) {
            String contactListStr = "";
            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");

                Log.d("Cursor position", cursor.getPosition() + " " + phonenumber);
                if (!phonenumber.contains("*") && !phonenumber.contains("#")) {
                    phonenumber = phonenumber.replaceAll("\\D", "");
                    if (contactListStr.isEmpty()) {
                        contactListStr = contactListStr + phonenumber;
                    } else {
                        contactListStr = contactListStr + "," + phonenumber;
                    }
                    // name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                }
            }
            cursor.close();
            System.out.println(Constants.ServiceTAG.CONTACTS + contactListStr);
            getContactSyncApiCall(contactListStr);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.Keys.READ_CONTACTS_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (syncChecked) {
                getContactsIntoArrayList();
//                }
            } else {
                // Permission was denied or request was cancelled
            }
        }

    }

    private void getContactSyncApiCall(String contactCSVList) {
        try {
            if (isApiRunning)
                return;
            isApiRunning = true;
//            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
//            progressDialog.show();
            swipeRefreshLayout.setRefreshing(true);
            final GetContactSyncApiCall apiCall = new GetContactSyncApiCall(userModel.getUserId(), contactCSVList);
            HttpRequestHandler.getInstance(getActivity().getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
//                    DialogUtils.hideProgressDialog(progressDialog);
                    swipeRefreshLayout.setRefreshing(false);
                    isApiRunning = false;
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(getActivity(), "Contact sync successfully.");
                            } else {
                                DialogUtils.showAlert(getActivity(), baseModel.getStatusMessage());
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
            isApiRunning = false;
        }
    }

    @Override
    public void onRefresh() {
        getFoodieFlashPosts();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            getContactsIntoArrayList();
        }
    }
}
