package com.homemadebazar.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.homemadebazar.R;

/**
 * Created by atulraj on 24/8/17.
 */

public class DialogUtils {

    public static AlertDialog.Builder dialog = null;

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

    public static void showFoodTimingsDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_save_edit_timings, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();


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

    public static void showScanDialog(Context context, String mobileNumber,final Runnable proceed, final Runnable close) {
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

    public static void bookFoodOnSelectedDatesDialog(){


    }

}
