package com.homemadebazar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homemadebazar.R;
import com.homemadebazar.activity.FoodieHomeChefSearchActivity;
import com.homemadebazar.adapter.ViewPagerAdapter;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.FoodieHomeChiefNearByListApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private UserModel userModel;
//    private FloatingActionButton fabSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_home, container, false);
    }

    @Override
    protected void initUI() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = getView().findViewById(R.id.tab_layout);
        viewPager = getView().findViewById(R.id.view_pager);
//        fabSearch = getView().findViewById(R.id.fab_search);

    }

    @Override
    protected void initialiseListener() {
        userModel = SharedPreference.getUserModel(getActivity());

//        fabSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FoodieHomeChefSearchActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void setData() {
        setUpViewPager();
        setUpTabLayout();
        getChiefDetailListApiCall();
    }

    private void setUpViewPager() {
        viewPagerAdapter.addFragment(new FoodieHomeMapFragment(), "Map");
        viewPagerAdapter.addFragment(new FoodieHomeListFragment(), "List");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    public void getChiefDetailListApiCall() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(getActivity(), null);
            progressDialog.show();

            final FoodieHomeChiefNearByListApiCall apiCall = new FoodieHomeChiefNearByListApiCall(userModel.getUserId(), "28.5244", "77.1855");
            HttpRequestHandler.getInstance(getActivity()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                // DialogUtils.showAlert(getActivity(), "HomeChiefDetailList size:-" + apiCall.getHomeChiefDetailList().size());
                                ((FoodieHomeMapFragment) viewPagerAdapter.getItem(0)).setFoodieMapDataList(apiCall.getHomeChiefDetailList());
                                ((FoodieHomeListFragment) viewPagerAdapter.getItem(1)).setFoodieHomeListDetails(apiCall.getHomeChiefDetailList());
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

}
