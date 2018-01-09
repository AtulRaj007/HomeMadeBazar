package com.homemadebazar.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.MarketPlaceProductAddBrandApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

public class MarketPlaceAddBrandActivity extends BaseActivity implements View.OnClickListener {
    private EditText etBrandName, etBrandDesc;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_add_brand);
        setupToolbar();
    }

    @Override
    protected void initUI() {
        userModel = SharedPreference.getUserModel(this);
        etBrandName = findViewById(R.id.et_brand_name);
        etBrandDesc = findViewById(R.id.et_brand_desc);
    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Add Brand");

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etBrandName.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter brand name.");
            return false;
        } else if (TextUtils.isEmpty(etBrandDesc.getText().toString().trim())) {
            DialogUtils.showAlert(this, "Please enter brand description.");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (isValid()) {
                    addBrandApiCall(etBrandName.getText().toString().trim(), etBrandDesc.getText().toString().trim());
                }
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    public void addBrandApiCall(String brandName, String brandDesc) {
        try {
            final ProgressDialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            final MarketPlaceProductAddBrandApiCall apiCall = new MarketPlaceProductAddBrandApiCall(userModel.getUserId(), brandName, brandDesc);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    DialogUtils.hideProgressDialog(progressDialog);
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (userModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                DialogUtils.showAlert(MarketPlaceAddBrandActivity.this, "Brand added successfully.", new Runnable() {
                                    @Override
                                    public void run() {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                DialogUtils.showAlert(MarketPlaceAddBrandActivity.this, userModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), MarketPlaceAddBrandActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), MarketPlaceAddBrandActivity.this, null);
        }
    }
}
