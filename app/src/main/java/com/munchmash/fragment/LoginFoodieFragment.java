package com.munchmash.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munchmash.R;

/**
 * Created by HP on 7/28/2017.
 */

public class LoginFoodieFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_foodie,container,false);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {

    }
}
