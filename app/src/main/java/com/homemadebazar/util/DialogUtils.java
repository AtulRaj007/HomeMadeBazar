package com.homemadebazar.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.homemadebazar.R;
import com.homemadebazar.activity.CreateOrderActivity;
import com.homemadebazar.adapter.HomeChefFoodTimingAdapter;
import com.homemadebazar.adapter.MyOrdersAdapter;
import com.homemadebazar.model.FoodDateTimeBookModel;
import com.homemadebazar.model.FoodTimingModel;
import com.homemadebazar.model.HomeChefOrderModel;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atulraj on 24/8/17.
 */

public class DialogUtils {

    public static AlertDialog.Builder dialog = null;
    public static String bookDate;
    private static TextView timeSelectedTextView = null;
    private static int rating = 0;

    public static void showFoodDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_food_layout, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private static String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

    public static void showFoodTimingsDialog(final Context context, final FoodTimingModel foodTimingModel, final CreateOrderActivity.FoodTimingEditInterface foodTimingEditInterface) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_save_edit_timings, null);
        final TextView tvBreakfastStartTime = view.findViewById(R.id.tv_breakfast_start_time);
        final TextView tvBreakfastEndTime = view.findViewById(R.id.tv_breakfast_end_time);
        final TextView tvLunchStartTime = view.findViewById(R.id.tv_lunch_start_time);
        final TextView tvLunchEndTime = view.findViewById(R.id.tv_lunch_end_time);
        final TextView tvDinnerStartTime = view.findViewById(R.id.tv_dinner_start_time);
        final TextView tvDinnerEndTime = view.findViewById(R.id.tv_dinner_end_time);

        tvBreakfastStartTime.setText(foodTimingModel.getBreakFastStartTime());
        tvBreakfastEndTime.setText(foodTimingModel.getBreakFastEndTime());
        tvLunchStartTime.setText(foodTimingModel.getLunchStartTime());
        tvLunchEndTime.setText(foodTimingModel.getLunchEndTime());
        tvDinnerStartTime.setText(foodTimingModel.getDinnerStartTime());
        tvDinnerEndTime.setText(foodTimingModel.getDinnerEndTime());

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                System.out.println("Hour of Day:-" + hourOfDay);
                System.out.println("Minute:-" + minute);
                System.out.println(timePicker.is24HourView());
                try {
                    if (timeSelectedTextView != null)
                        timeSelectedTextView.setText(getTime(hourOfDay, minute));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_breakfast_start_time:
                        timeSelectedTextView = tvBreakfastStartTime;
                        TimePickerDialog timerPicker = new TimePickerDialog(context, onTimeSetListener, 9, 30, false);
                        timerPicker.show();
                        break;
                    case R.id.tv_breakfast_end_time:
                        timeSelectedTextView = tvBreakfastEndTime;
                        new TimePickerDialog(context, onTimeSetListener, 11, 00, false).show();
                        break;
                    case R.id.tv_lunch_start_time:
                        timeSelectedTextView = tvLunchStartTime;
                        new TimePickerDialog(context, onTimeSetListener, 1, 00, false).show();
                        break;
                    case R.id.tv_lunch_end_time:
                        timeSelectedTextView = tvLunchEndTime;
                        new TimePickerDialog(context, onTimeSetListener, 2, 30, false).show();
                        break;
                    case R.id.tv_dinner_start_time:
                        timeSelectedTextView = tvDinnerStartTime;
                        new TimePickerDialog(context, onTimeSetListener, 7, 00, false).show();
                        break;
                    case R.id.tv_dinner_end_time:
                        timeSelectedTextView = tvDinnerEndTime;
                        new TimePickerDialog(context, onTimeSetListener, 9, 00, false).show();
                        break;
                }
            }
        };

        tvBreakfastStartTime.setOnClickListener(onClickListener);
        tvBreakfastEndTime.setOnClickListener(onClickListener);
        tvLunchStartTime.setOnClickListener(onClickListener);
        tvLunchEndTime.setOnClickListener(onClickListener);
        tvDinnerStartTime.setOnClickListener(onClickListener);
        tvDinnerEndTime.setOnClickListener(onClickListener);

        builder.setView(view);

        final AlertDialog dialog = builder.create();

        view.findViewById(R.id.btn_save_timings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodTimingModel foodTimingModel = new FoodTimingModel();
                foodTimingModel.setBreakFastStartTime(tvBreakfastStartTime.getText().toString().trim());
                foodTimingModel.setBreakFastEndTime(tvBreakfastEndTime.getText().toString().trim());
                foodTimingModel.setLunchStartTime(tvLunchStartTime.getText().toString().trim());
                foodTimingModel.setLunchEndTime(tvLunchEndTime.getText().toString().trim());
                foodTimingModel.setDinnerStartTime(tvDinnerStartTime.getText().toString().trim());
                foodTimingModel.setDinnerEndTime(tvDinnerEndTime.getText().toString().trim());
                timeSelectedTextView = null;
                foodTimingEditInterface.onFoodTimingSelected(foodTimingModel);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        if (context != null) {
            ProgressDialog dialog = new ProgressDialog(context, android.R.style.Theme_DeviceDefault_Dialog);
            dialog.setMessage(message == null ? context.getString(R.string.progress_dialog) : message);
            dialog.setCancelable(false);
            return dialog;
        }
        return null;
    }

    public static void hideProgressDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void showAlert(Context context, String message) {
        showAlert(context, message, null);
    }

    public static void showAlert(Context context, String message,
                                 final Runnable handler) {
        try {
            if (context != null && dialog == null) {
                dialog = new AlertDialog.Builder(context);
                dialog.setTitle(context.getString(R.string.app_name));
                dialog.setMessage(message);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog1, int which) {

                        if (handler != null) {
                            handler.run();
                        }
                        if (dialog != null) {
                            dialog = null;
                        }
                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        if (handler != null) {
                            handler.run();
                        }
                        if (dialog != null) {
                            dialog = null;
                        }
                    }
                });


                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(Context context, String message,
                                 final Runnable ok, final Runnable cancel) {
        if (context != null && dialog == null) {
            dialog = new AlertDialog.Builder(context);
            dialog.setTitle(context.getString(R.string.app_name));
            dialog.setMessage(message);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog1, int which) {

                    if (ok != null) {
                        ok.run();
                    }
                    if (dialog != null) {
                        dialog = null;
                    }
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (cancel != null) {
                        cancel.run();
                    }
                    if (dialog != null) {
                        dialog = null;
                    }
                }
            });
            dialog.show();


        }
    }

    public static void showAlert(Context context, String message, String title,
                                 final Runnable handler) {
        if (context != null) {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (handler != null) {
                        handler.run();
                    }
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    if (handler != null) {
                        handler.run();
                    }

                }
            });
            dialog.show();
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showOrderOtpDialog(final Context context, final MyOrdersAdapter.OtpSubmitInterface otpSubmitInterface) {
        final AlertDialog.Builder alertDBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_otp, null);
        alertDBuilder.setView(view);
        final Dialog dialog = alertDBuilder.create();

        final EditText etOtp = view.findViewById(R.id.et_otp);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etOtp.getText().toString().trim())) {
                    DialogUtils.showAlert(context, "Otp is empty");
                } else {
                    otpSubmitInterface.onOtpEnter(etOtp.getText().toString().trim());
                    dialog.dismiss();

                }
            }
        });

        dialog.show();
    }

    public static void showRatingDialog(final Context context, final MyOrdersAdapter.ReviewSubmitInterface reviewSubmitInterface) {
        final int ratingIds[] = {R.id.iv_first_rating, R.id.iv_second_rating, R.id.iv_third_rating, R.id.iv_fourth_rating, R.id.iv_fifth_rating};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View customview = LayoutInflater.from(context).inflate(R.layout.dialog_user_rating, null);
        builder.setView(customview);
        final EditText etReviewText = customview.findViewById(R.id.et_review_desc);
        final Dialog dialog = builder.create();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_first_rating:
                    case R.id.iv_second_rating:
                    case R.id.iv_third_rating:
                    case R.id.iv_fourth_rating:
                    case R.id.iv_fifth_rating:

                        String strTag = (String) view.getTag();
                        int tag = Integer.parseInt(strTag);
                        System.out.println("Tag:-" + tag);

                        for (int i = 0; i < 5; i++) {
                            if (i <= tag) {
                                ((ImageView) customview.findViewById(ratingIds[i])).setImageResource(R.drawable.star_fill);
                            } else {
                                ((ImageView) customview.findViewById(ratingIds[i])).setImageResource(R.drawable.star);
                            }
                        }

                        rating = tag + 1;

                        break;
                    case R.id.btn_submit:
                        String reviewText = etReviewText.getText().toString().trim();
                        if (rating == 0) {
                            DialogUtils.showAlert(context, "Please select the rating");
                            return;
                        } else if (TextUtils.isEmpty(reviewText)) {
                            DialogUtils.showAlert(context, "Please enter short review.");
                            return;
                        }

                        reviewSubmitInterface.onReviewSubmit(rating, reviewText);
                        rating = 0;
                        dialog.dismiss();

                        break;
                }
            }
        };

        for (int i = 0; i < ratingIds.length; i++) {
            customview.findViewById(ratingIds[i]).setOnClickListener(onClickListener);
        }

        customview.findViewById(R.id.btn_submit).setOnClickListener(onClickListener);

        builder.setCancelable(true);

        dialog.show();
    }

    public static void showMediaDialog(Context context, final Runnable camera, final Runnable gallary) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_media, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(true);

        TextView tvTakePhoto = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tvChooseFromGallary = (TextView) view.findViewById(R.id.tv_choose_from_gallary);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        final Dialog dialog = dialogBuilder.create();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_take_photo:
                        dialog.dismiss();
                        if (camera != null) {
                            camera.run();
                        }
                        break;
                    case R.id.tv_choose_from_gallary:
                        dialog.dismiss();
                        if (gallary != null) {
                            gallary.run();
                        }
                        break;
                    case R.id.tv_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };

        tvTakePhoto.setOnClickListener(onClickListener);
        tvChooseFromGallary.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);

        dialog.show();
    }

    public static void showScanDialog(Context context, String mobileNumber, final Runnable proceed, final Runnable close) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_scan_number, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);

        TextView tvNumber = view.findViewById(R.id.tv_mobile_number);
        tvNumber.setText(mobileNumber);

        final Dialog dialog = dialogBuilder.create();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_proceed:
                        dialog.dismiss();
                        if (proceed != null) {
                            proceed.run();
                        }
                        break;
                    case R.id.btn_close:
                        dialog.dismiss();
                        if (close != null) {
                            close.run();
                        }
                        break;
                }
            }
        };

        view.findViewById(R.id.btn_proceed).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_close).setOnClickListener(onClickListener);

        dialog.show();
    }

    private static double calcPrice(String price, String discount, int noOfPeople) {
        double calcPrice = 0.0;
        int intDiscount = 0;
        if (discount == null) {
            discount = "0";
        }
        try {
            calcPrice = Double.parseDouble(price);
            intDiscount = Integer.parseInt(discount);
            calcPrice = (calcPrice * noOfPeople) * (100 - intDiscount) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total Price=" + calcPrice);
        return calcPrice;
    }

    public static void bookFoodOnSelectedDatesDialog(final Context context, final HomeChefOrderModel homeChefOrderModel, final HomeChefFoodTimingAdapter.BookOrderInterface bookOrderInterface, final String foodTimeType) {

        int radioButtonIds[] = {R.id.radiobutton_one, R.id.radiobutton_two, R.id.radiobutton_three, R.id.radiobutton_four, R.id.radiobutton_five, R.id.radiobutton_six, R.id.radiobutton_seven};

        final ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels = Utils.parseFoodBookDateTime(homeChefOrderModel.getDishAvailability());

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_food_dates, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(true);

        final Dialog dialog = dialogBuilder.create();
        final RadioGroup rgDinnerDate = view.findViewById(R.id.rg_dinner_date);
        final RadioGroup rgDinnerTime = view.findViewById(R.id.rg_dinner_time);

        final TextView tvNoOfPeople = view.findViewById(R.id.tv_no_of_people);
        final TextView tvPrice = view.findViewById(R.id.tv_price);
        final TextView tvDiscount = view.findViewById(R.id.tv_discount);
        final TextView tvTotal = view.findViewById(R.id.tv_total);

        if (homeChefOrderModel.getDiscount() == null)
            homeChefOrderModel.setDiscount("0");
        tvPrice.setText(homeChefOrderModel.getPrice());
        tvDiscount.setText(homeChefOrderModel.getDiscount());

        tvTotal.setText(calcPrice(homeChefOrderModel.getPrice(), homeChefOrderModel.getDiscount(), 1) + "");

        rgDinnerDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radiobutton_one:

                        if (!foodDateTimeBookModels.get(0).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(0).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(0).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_two:

                        if (!foodDateTimeBookModels.get(1).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(1).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(1).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_three:

                        if (!foodDateTimeBookModels.get(2).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(2).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(2).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_four:

                        if (!foodDateTimeBookModels.get(3).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(3).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(3).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_five:

                        if (!foodDateTimeBookModels.get(4).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(4).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(4).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_six:

                        if (!foodDateTimeBookModels.get(5).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(5).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(5).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                    case R.id.radiobutton_seven:

                        if (!foodDateTimeBookModels.get(6).isBreakFast()) {
                            view.findViewById(R.id.rg_breakfast).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_breakfast).setEnabled(true);
                        }
                        if (!foodDateTimeBookModels.get(6).isLunch()) {
                            view.findViewById(R.id.rg_lunch).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_lunch).setEnabled(true);
                        }

                        if (!foodDateTimeBookModels.get(6).isDinner()) {
                            view.findViewById(R.id.rg_dinner).setEnabled(false);
                        } else {
                            view.findViewById(R.id.rg_dinner).setEnabled(true);
                        }

                        break;
                }
            }
        });


        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        System.out.println("Current Date:-" + currentDate);

        for (int i = 0; i < radioButtonIds.length; i++) {
            ((RadioButton) view.findViewById(radioButtonIds[i])).setText(foodDateTimeBookModels.get(i).getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date bookingDate = sdf.parse(foodDateTimeBookModels.get(i).getDate());
                if (bookingDate.before(sdf.parse(currentDate))) {
                    view.findViewById(radioButtonIds[i]).setEnabled(false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int noOfPeople = Integer.parseInt(tvNoOfPeople.getText().toString().trim());
                    if (noOfPeople >= 20) {
                        return;
                    }
                    noOfPeople++;
                    tvNoOfPeople.setText(String.valueOf(noOfPeople));
                    tvTotal.setText(calcPrice(homeChefOrderModel.getPrice(), homeChefOrderModel.getDiscount(), noOfPeople) + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        view.findViewById(R.id.iv_substract).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfPeople = Integer.parseInt(tvNoOfPeople.getText().toString().trim());
                if (noOfPeople <= 1)
                    return;
                noOfPeople--;
                tvNoOfPeople.setText(String.valueOf(noOfPeople));
                tvTotal.setText(calcPrice(homeChefOrderModel.getPrice(), homeChefOrderModel.getDiscount(), noOfPeople) + "");

            }
        });

        view.findViewById(R.id.btn_book_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dinnerDateId = rgDinnerDate.getCheckedRadioButtonId();
                int dinnerTimeId = rgDinnerTime.getCheckedRadioButtonId();

                if (dinnerDateId == -1) {
                    DialogUtils.showAlert(context, "Please select book date.");
                    return;
                } else if (dinnerTimeId == -1) {
                    DialogUtils.showAlert(context, "Please select food timing.");
                    return;
                }
                System.out.println(">>>>>Date Selected Id:-" + dinnerDateId);
                System.out.println(">>>>>Time Id:-" + dinnerTimeId);
                String dinnerDate = getDinnerDateFromId(dinnerDateId, foodDateTimeBookModels);
                int dinnerTime = getDinnerTimeFromId(dinnerTimeId, foodDateTimeBookModels);
                System.out.println(">>>>>Dinner Date:-" + dinnerDate);
                System.out.println(">>>>>Time Id:-" + dinnerTime);

                if (dinnerTime == -1) {
                    // BreakFast Lunch Dinner
                    bookOrderInterface.onOrderSelected(dinnerDate, Integer.parseInt(foodTimeType), parseStringToInt(tvNoOfPeople.getText().toString().trim()));
                } else {
                    // Discover Hot Deals
                    bookOrderInterface.onOrderSelected(dinnerDate, dinnerTime, parseStringToInt(tvNoOfPeople.getText().toString().trim()));
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private static int parseStringToInt(String strInteger) {
        try {
            return Integer.parseInt(strInteger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static String getDinnerDateFromId(int id, ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels) {
        String date = "";
        switch (id) {
            case R.id.radiobutton_one:
                date = foodDateTimeBookModels.get(0).getDate();
                break;
            case R.id.radiobutton_two:
                date = foodDateTimeBookModels.get(1).getDate();
                break;
            case R.id.radiobutton_three:
                date = foodDateTimeBookModels.get(2).getDate();
                break;
            case R.id.radiobutton_four:
                date = foodDateTimeBookModels.get(3).getDate();
                break;
            case R.id.radiobutton_five:
                date = foodDateTimeBookModels.get(4).getDate();
                break;
            case R.id.radiobutton_six:
                date = foodDateTimeBookModels.get(5).getDate();
                break;
            case R.id.radiobutton_seven:
                date = foodDateTimeBookModels.get(6).getDate();
                break;
        }
        return date;
    }

    /* Not Working */
    private static int getDinnerTimeFromId(int id, ArrayList<FoodDateTimeBookModel> foodDateTimeBookModels) {
        String time = "";
        switch (id) {
            case R.id.rg_breakfast:
                return Constants.DinnerTime.BREAKFAST;
            case R.id.rg_lunch:
                return Constants.DinnerTime.LUNCH;
            case R.id.rg_dinner:
                return Constants.DinnerTime.DINNER;
        }
        return -1;
    }
}
