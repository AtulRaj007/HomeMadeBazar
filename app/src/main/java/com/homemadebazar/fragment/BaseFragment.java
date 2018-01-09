package com.homemadebazar.fragment;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by HP on 7/28/2017.
 */

public abstract class BaseFragment extends Fragment {
    protected abstract void initUI();
    protected abstract void initialiseListener();
    protected abstract void setData();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        initialiseListener();
        setData();
    }
}
