package com.munchmash.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by HP on 7/28/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract void initUI();
    protected abstract void initialiseListener();
    protected abstract void setData();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initUI();
        initialiseListener();
        setData();
    }
}
