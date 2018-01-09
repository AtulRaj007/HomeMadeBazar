package com.homemadebazar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.SharedPreference;

public class MyProfileActivity extends BaseActivity {
    private UserModel userModel;

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
        ((TextView) findViewById(R.id.tv_title)).setText("");

    }

    @Override
    protected void initUI() {
        userModel= SharedPreference.getUserModel(MyProfileActivity.this);
        if(userModel!=null) {
            ((EditText) findViewById(R.id.et_first_name)).setText(userModel.getFirstName());
            ((EditText) findViewById(R.id.et_last_name)).setText(userModel.getLastName());
            ((EditText) findViewById(R.id.et_email_id)).setText(userModel.getEmailId());
            ((EditText) findViewById(R.id.et_phone)).setText(userModel.getMobile());
            ((EditText) findViewById(R.id.et_country)).setText(userModel.getCountryName());
        }
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {

    }
}
