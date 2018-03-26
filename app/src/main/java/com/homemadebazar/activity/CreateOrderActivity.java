package com.homemadebazar.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.homemadebazar.R;
import com.homemadebazar.Template.Template;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.CreateOrderModel;
import com.homemadebazar.model.FoodCategoryModel;
import com.homemadebazar.model.FoodTimingModel;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.network.GetRequest;
import com.homemadebazar.network.HttpRequestHandler;
import com.homemadebazar.network.MultiPartRequest;
import com.homemadebazar.network.api.ApiCall;
import com.homemadebazar.network.apicall.HomeChefCreateOrderApiCall;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.JSONParsingUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

    private UserModel userModel;
    private EditText etDishName, etFirstRule, etSecondRule, etThirdRule, etFourthRule, etFifthRule, etDescription;
    private Spinner sprDishCategory, sprDishPrice, sprVeg, sprDrinks, sprMinNoOfGuest, sprMaxNoOfGuest, sprDiscount;
    private Switch switchPetsAllowed;
    private String foodType[] = {"Veg", "NonVeg"};
    private RequestQueue mRequest;
    private MultiPartRequest mMultiPartRequest;
    private ArrayList<File> mFile = new ArrayList<File>();
    private String coverPhotoArray[] = new String[5];
    private int imageSelectedIndex = 0;
    private String dishAvailability = "";
    private ImageView ivFirstFoodPhoto, ivSecondFoodPhoto, ivThirdFoodPhoto, ivFourthFoodPhoto, ivFifthFoodPhoto;
    private TextView tvBreakfastDuration, tvLunchDuration, tvDinnerDuration;
    private static FoodTimingModel foodTimingModel = null;
    private int resourceIds[] =
            {

                    R.id.iv_mon_breakfast,
                    R.id.iv_mon_lunch,
                    R.id.iv_mon_dinner,

                    R.id.iv_tue_breakfast,
                    R.id.iv_tue_lunch,
                    R.id.iv_tue_dinner,

                    R.id.iv_wed_breakfast,
                    R.id.iv_wed_lunch,
                    R.id.iv_wed_dinner,

                    R.id.iv_thu_breakfast,
                    R.id.iv_thu_lunch,
                    R.id.iv_thu_dinner,

                    R.id.iv_fri_breakfast,
                    R.id.iv_fri_lunch,
                    R.id.iv_fri_dinner,

                    R.id.iv_sat_breakfast,
                    R.id.iv_sat_lunch,
                    R.id.iv_sat_dinner,

                    R.id.iv_sun_breakfast,
                    R.id.iv_sun_lunch,
                    R.id.iv_sun_dinner

            };
    private int dateIndex = 0;
    private String drinks[] = {"Alcoholic", "Non-Alcoholic"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        setupToolbar();
    }

    @Override
    protected void initUI() {

        foodTimingModel = new FoodTimingModel();
        mRequest = Volley.newRequestQueue(CreateOrderActivity.this);
        initialiseFoodAvailability();
        userModel = SharedPreference.getUserModel(CreateOrderActivity.this);

        etDishName = findViewById(R.id.et_dish_name);
        etFirstRule = findViewById(R.id.et_rules_one);
        etSecondRule = findViewById(R.id.et_rules_two);
        etThirdRule = findViewById(R.id.et_rules_three);
        etFourthRule = findViewById(R.id.et_rules_four);
        etFifthRule = findViewById(R.id.et_rules_five);
        etDescription = findViewById(R.id.et_description);

        sprDishCategory = findViewById(R.id.spr_dish_category);
        sprDishPrice = findViewById(R.id.spr_dish_price);
        sprVeg = findViewById(R.id.spr_veg_nonveg);
        sprDrinks = findViewById(R.id.spr_drinks);
        sprMinNoOfGuest = findViewById(R.id.spr_min_no_guest);
        sprMaxNoOfGuest = findViewById(R.id.spr_max_no_guest);
        sprDiscount = findViewById(R.id.spr_discount);

        switchPetsAllowed = findViewById(R.id.switch_pets_allowed);

        ivFirstFoodPhoto = findViewById(R.id.iv_first_food_photo);
        ivSecondFoodPhoto = findViewById(R.id.iv_second_food_photo);
        ivThirdFoodPhoto = findViewById(R.id.iv_third_food_photo);
        ivFourthFoodPhoto = findViewById(R.id.iv_fourth_food_photo);
        ivFifthFoodPhoto = findViewById(R.id.iv_fifth_food_photo);

        tvBreakfastDuration = findViewById(R.id.tv_breakfast_duration);
        tvLunchDuration = findViewById(R.id.tv_lunch_duration);
        tvDinnerDuration = findViewById(R.id.tv_dinner_duration);

    }

    @Override
    protected void initialiseListener() {
        findViewById(R.id.btn_post_order).setOnClickListener(this);
        findViewById(R.id.btn_edit_timings).setOnClickListener(this);

        findViewById(R.id.iv_first_food_photo).setOnClickListener(this);
        findViewById(R.id.iv_second_food_photo).setOnClickListener(this);
        findViewById(R.id.iv_third_food_photo).setOnClickListener(this);
        findViewById(R.id.iv_fourth_food_photo).setOnClickListener(this);
        findViewById(R.id.iv_fifth_food_photo).setOnClickListener(this);
    }

    private void setupToolbar() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_title)).setText("Create Order");

    }

    @Override
    protected void setData() {

        if (foodTimingModel != null) {
            tvBreakfastDuration.setText(foodTimingModel.getBreakFastStartTime() + " - " + foodTimingModel.getBreakFastEndTime());
            tvLunchDuration.setText(foodTimingModel.getLunchStartTime() + " - " + foodTimingModel.getLunchEndTime());
            tvDinnerDuration.setText(foodTimingModel.getDinnerStartTime() + " - " + foodTimingModel.getDinnerEndTime());
        }

        DialogUtils.showFoodDialog(CreateOrderActivity.this);
        getFoodCategories();

        /* Spinner Adapter */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateOrderActivity.this, R.layout.simple_list_item, foodType);
        sprVeg.setAdapter(adapter);

        ArrayAdapter<String> drinksAdapter = new ArrayAdapter<String>(CreateOrderActivity.this, R.layout.simple_list_item, drinks);
        sprDrinks.setAdapter(drinksAdapter);

        ArrayAdapter<String> minGuestAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, getGuestArray("Min Guest"));
        sprMinNoOfGuest.setAdapter(minGuestAdapter);

        ArrayAdapter<String> maxGuestAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, getGuestArray("Max Guest"));
        sprMaxNoOfGuest.setAdapter(maxGuestAdapter);

        ArrayList<String> priceArrayList = Utils.getPriceArray("Price");
        ArrayAdapter<String> dishPriceAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, priceArrayList);
        sprDishPrice.setAdapter(dishPriceAdapter);

        ArrayAdapter<String> discountAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, getDiscountArrayList("Discount (%)"));
        sprDiscount.setAdapter(discountAdapter);

    }

    private void initialiseFoodAvailability() {
        for (int i = 0; i < resourceIds.length; i++) {
            findViewById(resourceIds[i]).setTag(Constants.FoodTiming.Unselected);
            findViewById(resourceIds[i]).setOnClickListener(this);
        }

    }

    private ArrayList<String> getDiscountArrayList(String title) {
        ArrayList<String> discountArrayList = new ArrayList<>();
        discountArrayList.add(String.valueOf(0));
        discountArrayList.add(title);
        for (int i = 5; i <= 50; i++) {
            discountArrayList.add(String.valueOf(i));
        }
        return discountArrayList;
    }

    private ArrayList<String> getGuestArray(String title) {
        ArrayList<String> guestArrayList = new ArrayList<>();
        guestArrayList.add(title);
        for (int i = 1; i <= 50; i++) {
            guestArrayList.add(String.valueOf(i));
        }
        return guestArrayList;
    }

    private String getDishDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, dateIndex);
        String day = sdf.format(calendar.getTime());
        Log.i("Selected Day:-", day);
        dateIndex++;
        return day;
    }

    private String getIndexDate(int postion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, postion);
        String day = sdf.format(calendar.getTime());
        Log.i("Selected Day:-", day);
        dateIndex++;
        return day;
    }

    private void printFoodSelectedDetails() {
        int index = 0;
        dishAvailability = "";
        dateIndex = 0;
        for (int i = 0; i < resourceIds.length; i++) {
//            System.out.println(i + "====" + findViewById(resourceIds[i]).getTag());
            index++;
            if (index == 1) {
                dishAvailability = dishAvailability + getDishDate() + ",";
                dishAvailability = dishAvailability + findViewById(resourceIds[i]).getTag() + ",";
            } else {
                if (index == 3) {
                    index = 0;
                    if (i == resourceIds.length - 1) {
                        dishAvailability = dishAvailability + findViewById(resourceIds[i]).getTag();
                    } else {
                        dishAvailability = dishAvailability + findViewById(resourceIds[i]).getTag() + ";";
                    }
                } else {
                    dishAvailability = dishAvailability + findViewById(resourceIds[i]).getTag() + ",";
                }
            }

//            dishAvailability = dishAvailability + findViewById(resourceIds[i]).getTag();
        }
        System.out.println("====Dish Availability====" + dishAvailability);
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etDishName.getText().toString().trim())) {
            DialogUtils.showAlert(CreateOrderActivity.this, "Please enter dish name.");
            return false;
        } else if (TextUtils.isEmpty(etDescription.getText().toString().trim())) {
            DialogUtils.showAlert(CreateOrderActivity.this, "Please enter description.");
            return false;
        } else if (mFile.size() == 0) {
            DialogUtils.showAlert(CreateOrderActivity.this, "Please select food images.");
            return false;
        } else if (dishAvailability.equals("0")) {
            DialogUtils.showAlert(CreateOrderActivity.this, "Please select Food Timings.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_post_order:
                printFoodSelectedDetails();
                if (isValid()) {
                    createOrder();
                }

                break;
            case R.id.btn_edit_timings:

                DialogUtils.showFoodTimingsDialog(CreateOrderActivity.this, foodTimingModel, new FoodTimingEditInterface() {
                    @Override
                    public void onFoodTimingSelected(FoodTimingModel foodTimingModel) {
                        CreateOrderActivity.foodTimingModel = foodTimingModel;
                    }
                });
                break;

            case R.id.iv_first_food_photo:
                imageSelectedIndex = 0;
                DialogUtils.showMediaDialog(CreateOrderActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(CreateOrderActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(CreateOrderActivity.this);
                    }
                });

                break;

            case R.id.iv_second_food_photo:
                imageSelectedIndex = 1;
                DialogUtils.showMediaDialog(CreateOrderActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(CreateOrderActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(CreateOrderActivity.this);
                    }
                });
                break;

            case R.id.iv_third_food_photo:
                imageSelectedIndex = 2;
                DialogUtils.showMediaDialog(CreateOrderActivity.this, new Runnable() {
                    @Override
                    public void run() {
//                        etGuestRules.getText().toString().trim()
                        // Camera
                        Utils.cameraIntent(CreateOrderActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(CreateOrderActivity.this);
                    }
                });
                break;

            case R.id.iv_fourth_food_photo:
                imageSelectedIndex = 3;
                DialogUtils.showMediaDialog(CreateOrderActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(CreateOrderActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // GallaryetGuestRules.getText().toString().trim()
                        Utils.gallaryIntent(CreateOrderActivity.this);
                    }
                });
                break;
            case R.id.iv_fifth_food_photo:
                imageSelectedIndex = 4;
                DialogUtils.showMediaDialog(CreateOrderActivity.this, new Runnable() {
                    @Override
                    public void run() {
                        // Camera
                        Utils.cameraIntent(CreateOrderActivity.this);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        // Gallary
                        Utils.gallaryIntent(CreateOrderActivity.this);
                    }
                });
                break;
            default:
                if ((int) view.getTag() == Constants.FoodTiming.Unselected) {
//                    etGuestRules.getText().toString().trim()
                    ((ImageView) view).setBackgroundColor(getResources().getColor(R.color.white));
                    ((ImageView) view).setImageResource(R.drawable.ic_date_selected);
                    view.setTag(Constants.FoodTiming.Selected);
                } else {
                    ((ImageView) view).setImageDrawable(null);
                    ((ImageView) view).setBackground(getResources().getDrawable(R.drawable.background_border_widet));
                    view.setTag(Constants.FoodTiming.Unselected);
                }
        }
    }

    private void getFoodCategories() {
        new GetRequest(CreateOrderActivity.this, new GetRequest.ApiCompleteListener() {
            @Override
            public void onApiCompleteListener(String response) {
                System.out.println("====== Categories ======" + response);
                ArrayList<FoodCategoryModel> foodCategoryModelArrayList = JSONParsingUtils.parseFoodCategoryModel(response);
                ArrayAdapter<FoodCategoryModel> spinnerAdapter = new ArrayAdapter<FoodCategoryModel>(CreateOrderActivity.this, android.R.layout.simple_list_item_1, foodCategoryModelArrayList);
                spinnerAdapter.setDropDownViewResource(R.layout
                        .simple_list_item);
                sprDishCategory.setAdapter(spinnerAdapter);
            }
        }).execute(Constants.ServerURL.GET_FOOD_CATEGORIES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.Keys.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    setImage(Utils.getCameraUri());
                } else {
                    DialogUtils.showAlert(CreateOrderActivity.this, "Camera Cancelled");
                }
                break;
            case Constants.Keys.REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    System.out.println();
                    Uri uri = data.getData();
                    setImage(uri);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImage(Uri uri) {

        mFile.add(new File(Utils.getPath(CreateOrderActivity.this, uri)));
        switch (imageSelectedIndex) {
            case 0:
                ivFirstFoodPhoto.setImageURI(uri);
                coverPhotoArray[0] = uri.getPath();
                break;
            case 1:
                ivSecondFoodPhoto.setImageURI(uri);
                coverPhotoArray[1] = uri.getPath();
                break;
            case 2:
                ivThirdFoodPhoto.setImageURI(uri);
                coverPhotoArray[2] = uri.getPath();
                break;
            case 3:
                ivFourthFoodPhoto.setImageURI(uri);
                coverPhotoArray[3] = uri.getPath();
                break;
            case 4:
                ivFifthFoodPhoto.setImageURI(uri);
                coverPhotoArray[4] = uri.getPath();
                break;
        }
    }

    private CreateOrderModel getCreateOrderModel() {
        CreateOrderModel createOrderModel = new CreateOrderModel();
        createOrderModel.setDishName(etDishName.getText().toString().trim());
        createOrderModel.setFoodCategoryId(((FoodCategoryModel) sprDishCategory.getSelectedItem()).getFoodCategoryId());
        createOrderModel.setDishPrice(sprDishPrice.getSelectedItem().toString());
        createOrderModel.setMinNoGuest(sprMinNoOfGuest.getSelectedItem().toString());
        createOrderModel.setMaxNoGuest(sprMaxNoOfGuest.getSelectedItem().toString());
        createOrderModel.setDiscount(sprDiscount.getSelectedItem().toString());
        createOrderModel.setPetsAllowed(switchPetsAllowed.isChecked());//
        createOrderModel.setDrinks(sprDrinks.getSelectedItem().toString());
        createOrderModel.setVeg(sprVeg.getSelectedItemPosition() == 0 ? true : false); //
        createOrderModel.setGuestRules(getRulesForOrder());
        createOrderModel.setDescription(etDescription.getText().toString().trim());
        createOrderModel.setDishAvailableDay(dishAvailability);//

        createOrderModel.setBreakFastTime(foodTimingModel.getBreakFastStartTime() + " - " + foodTimingModel.getBreakFastEndTime());
        createOrderModel.setLunchTime(foodTimingModel.getLunchStartTime() + " - " + foodTimingModel.getLunchEndTime());
        createOrderModel.setDinnerTime(foodTimingModel.getDinnerStartTime() + " - " + foodTimingModel.getDinnerEndTime());

        return createOrderModel;
    }


    private String getRulesForOrder() {
        String rules = "";
        String SYMBOL = "@@";
        if (!TextUtils.isEmpty(etFirstRule.getText().toString().trim()))
            rules = rules + etFirstRule.getText().toString().trim() + SYMBOL;
        if (!TextUtils.isEmpty(etSecondRule.getText().toString().trim()))
            rules = rules + etSecondRule.getText().toString().trim() + SYMBOL;
        if (!TextUtils.isEmpty(etThirdRule.getText().toString().trim()))
            rules = rules + etThirdRule.getText().toString().trim() + SYMBOL;
        if (!TextUtils.isEmpty(etFourthRule.getText().toString().trim()))
            rules = rules + etFourthRule.getText().toString().trim() + SYMBOL;
        if (!TextUtils.isEmpty(etFifthRule.getText().toString().trim()))
            rules = rules + etFifthRule.getText().toString().trim();

        return rules;
    }

    public interface FoodTimingEditInterface {
        void onFoodTimingSelected(FoodTimingModel foodTimingModel);
    }

    public void createOrder() {
        try {
            final Dialog progressDialog = DialogUtils.getProgressDialog(this, null);
            progressDialog.show();

            CreateOrderModel createOrderModel = getCreateOrderModel();
            final HomeChefCreateOrderApiCall apiCall = new HomeChefCreateOrderApiCall(userModel.getUserId(), createOrderModel);
            HttpRequestHandler.getInstance(this.getApplicationContext()).executeRequest(apiCall, new ApiCall.OnApiCallCompleteListener() {

                @Override
                public void onComplete(Exception e) {
                    progressDialog.hide();
                    if (e == null) { // Success
                        try {
                            BaseModel baseModel = apiCall.getResult();
                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
//                                DialogUtils.showAlert(CreateOrderActivity.this, "Order Id is:-" + apiCall.getOrderId());
                                uploadFile(apiCall.getOrderId());
                            } else {
                                DialogUtils.showAlert(CreateOrderActivity.this, baseModel.getStatusMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else { // Failure
                        Utils.handleError(e.getMessage(), CreateOrderActivity.this, null);
                    }
                }
            });
        } catch (Exception e) {
            Utils.handleError(e.getMessage(), CreateOrderActivity.this, null);
        }
    }

    private String getFoodImageUploadUrl(String orderId) {
        return Constants.uploadImageURL.FOOD_PHOTO_IMAGE_UPLOAD + userModel.getUserId() + "&" + "&OrderId=" + orderId;
    }

    void uploadFile(final String orderId) {
        final Dialog progressDialog = DialogUtils.getProgressDialog(this, "Uploading photo .... Please wait");
        progressDialog.show();

        mRequest.start();
        mMultiPartRequest = new MultiPartRequest(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                System.out.println(">>>>> Food Image Upload Failure =====");
                DialogUtils.showAlert(CreateOrderActivity.this, "Order Created Successfully but Image upload failed" + "\n" + "Order Id :-" + orderId, new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        }, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                progressDialog.hide();
                System.out.println(">>>>> Food Image Upload Success =====");
                DialogUtils.showAlert(CreateOrderActivity.this, "Order Created Successfully" + "\n" + "Order Id :-" + orderId, new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                });


            }
        }, mFile, mFile.size(), getFoodImageUploadUrl(orderId));

        mMultiPartRequest.setTag("MultiRequest");

        mMultiPartRequest.setRetryPolicy(new DefaultRetryPolicy(Template.VolleyRetryPolicy.SOCKET_TIMEOUT,
                Template.VolleyRetryPolicy.RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequest.add(mMultiPartRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            foodTimingModel = null;
        }
    }
}
