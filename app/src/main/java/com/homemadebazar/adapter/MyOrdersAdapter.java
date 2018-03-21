package com.homemadebazar.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.BaseModel;
import com.homemadebazar.model.HomeChefIncomingOrderModel;
import com.homemadebazar.model.UserLocation;
import com.homemadebazar.model.UserModel;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.DialogUtils;
import com.homemadebazar.util.ServiceUtils;
import com.homemadebazar.util.SharedPreference;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;

/**
 * Created by Sumit on 30/07/17.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private boolean isScheduledFragment = false;
    private ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList;
    private UserModel userModel;

    public MyOrdersAdapter(Context context, boolean isScheduledFragment, ArrayList<HomeChefIncomingOrderModel> homeChefIncomingOrderModelArrayList) {
        this.context = context;
        this.isScheduledFragment = isScheduledFragment;
        this.homeChefIncomingOrderModelArrayList = homeChefIncomingOrderModelArrayList;
        userModel = SharedPreference.getUserModel(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_scheduled_separator_view, parent, false);
            return new TitleSeparatorViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_homechef_foodie_orders, parent, false);
            return new MyOrdersViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeChefIncomingOrderModel homeChefIncomingOrderModel = homeChefIncomingOrderModelArrayList.get(position);
        if (holder instanceof MyOrdersViewHolder) {


            if (userModel.getAccountType().equals(Constants.Role.HOME_CHEF.getStringRole())) {
                if (!TextUtils.isEmpty(homeChefIncomingOrderModel.getFoodiesDp())) {
                    Glide.with(context).load(homeChefIncomingOrderModel.getFoodiesDp()).into(((MyOrdersViewHolder) holder).ivProfilePic);
                }
                ((MyOrdersViewHolder) holder).tvName.setText(homeChefIncomingOrderModel.getFoodiesFirstName() + " " + homeChefIncomingOrderModel.getFoodiesLastName());
                ((MyOrdersViewHolder) holder).tvMobileNumber.setText(homeChefIncomingOrderModel.getFoodieMobileNumber());
                ((MyOrdersViewHolder) holder).tvEmailId.setText(homeChefIncomingOrderModel.getFoodieEmailId());
            } else {
                if (!TextUtils.isEmpty(homeChefIncomingOrderModel.getFoodiesDp())) {
                    Glide.with(context).load(homeChefIncomingOrderModel.getFoodiesDp()).into(((MyOrdersViewHolder) holder).ivProfilePic);
                }
                ((MyOrdersViewHolder) holder).tvName.setText("");
                ((MyOrdersViewHolder) holder).tvMobileNumber.setText("");
                ((MyOrdersViewHolder) holder).tvEmailId.setText("");
            }
            ((MyOrdersViewHolder) holder).tvOrderType.setText(homeChefIncomingOrderModel.getOrderFor());
            ((MyOrdersViewHolder) holder).tvNoOfGuest.setText("No Of Guest:-" + homeChefIncomingOrderModel.getNoOfGuest());
            ((MyOrdersViewHolder) holder).tvPrice.setText(homeChefIncomingOrderModel.getPrice());
            ((MyOrdersViewHolder) holder).tvOrderTiming.setText(homeChefIncomingOrderModel.getEatingTime());
            ((MyOrdersViewHolder) holder).tvDiscount.setText(homeChefIncomingOrderModel.getDiscAmount() + "(%)");
            ((MyOrdersViewHolder) holder).tvOrderId.setText(homeChefIncomingOrderModel.getOrderId());
            ((MyOrdersViewHolder) holder).tvRequestId.setText(homeChefIncomingOrderModel.getOrderReqDate());
            ((MyOrdersViewHolder) holder).tvBookingDate.setText(homeChefIncomingOrderModel.getOrderRequestDate());
            ((MyOrdersViewHolder) holder).tvBookedFor.setText(homeChefIncomingOrderModel.getBookedDate());

            orderStatusHandling(homeChefIncomingOrderModel.getRequestStatus().trim(), holder, homeChefIncomingOrderModel.getOtp().trim());


        } else if (holder instanceof TitleSeparatorViewHolder) {
            ((TitleSeparatorViewHolder) holder).tvTitle.setText(homeChefIncomingOrderModel.getDateTitle());
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (homeChefIncomingOrderModelArrayList.get(position).getType() == 0)
            return 2;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return homeChefIncomingOrderModelArrayList.size();
    }

    private void foodieCancelOrder(final int position) {
        ServiceUtils.foodieOrderAcceptReject(context, userModel.getUserId(), homeChefIncomingOrderModelArrayList.get(position).getOrderRequestId(),
                Constants.OrderActionType.HC_ACCEPTED_ORDER, "", new ServiceUtils.OrderActionInterface() {
                    @Override
                    public void onOrderAction(BaseModel baseModel) {
                        if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                            DialogUtils.showAlert(context, "Your Order is cancelled Successfully", new Runnable() {
                                @Override
                                public void run() {
                                    homeChefIncomingOrderModelArrayList.get(position).setRequestStatus(Constants.OrderActionType.FOODIE_CANCELLED_ORDER);
                                    notifyDataSetChanged();
                                }
                            });
                        } else {
                            DialogUtils.showAlert(context, baseModel.getStatusMessage());
                        }
                    }
                });
    }

    private void orderStatusHandling(String orderStatus, RecyclerView.ViewHolder holder, String otp) {
        String accountType = userModel.getAccountType();
        if (accountType.equals(Constants.Role.HOME_CHEF.getStringRole())) {
            // HomeChef
            if (orderStatus.equals(Constants.OrderActionType.FOODIE_BOOKED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("");
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.GONE);


            } else if (orderStatus.equals(Constants.OrderActionType.FOODIE_CANCELLED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("Foodie has cancelled the order");

            } else if (orderStatus.equals(Constants.OrderActionType.HC_ACCEPTED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.VISIBLE);//
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);//GONE
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("");
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.GONE);


            } else if (orderStatus.equals(Constants.OrderActionType.HC_REJECTED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);

                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("You have rejected the order");

            } else if (orderStatus.equals(Constants.OrderActionType.HC_COMPLETED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);

                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("Order Completed");

            } else if (orderStatus.equals(Constants.OrderActionType.PENDING_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);

                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("Order Pending");
            }
        } else {
            // Foodie
            if (orderStatus.equals(Constants.OrderActionType.FOODIE_BOOKED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("");


            } else if (orderStatus.equals(Constants.OrderActionType.FOODIE_CANCELLED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("You have cancelled the order");

            } else if (orderStatus.equals(Constants.OrderActionType.HC_ACCEPTED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("You order is Accepted by HomeChef.\n Show the otp to the homechef\n Otp is " + otp);

            } else if (orderStatus.equals(Constants.OrderActionType.HC_REJECTED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("You order is rejected by the HomeChef");


            } else if (orderStatus.equals(Constants.OrderActionType.HC_COMPLETED_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("Order Completed");

            } else if (orderStatus.equals(Constants.OrderActionType.PENDING_ORDER)) {
                ((MyOrdersViewHolder) holder).rlHCAcceptReject.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlHcCompleteOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).rlFoodieCancelOrder.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).ivShowDirections.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).ivGiveReview.setVisibility(View.GONE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setVisibility(View.VISIBLE);
                ((MyOrdersViewHolder) holder).tvOrderStatus.setText("Pending Order");

            }
        }
    }

    private void makeCall(String mobileNumber) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, Constants.Keys.REQUEST_CALL_PHONE);
        } else {
            Utils.startCall(context, mobileNumber);
        }
    }

    private void message() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        context.startActivity(sendIntent);
    }

    public interface OtpSubmitInterface {
        void onOtpEnter(String otp);
    }

    public interface ReviewSubmitInterface {
        void onReviewSubmit(int rating, String reviewDesc);
    }

    class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProfilePic, ivCall, ivMessage, ivShowDirections, ivGiveReview;
        private TextView tvName, tvMobileNumber, tvEmailId, tvOrderType, tvNoOfGuest, tvPrice, tvOrderTiming, tvDiscount, tvOrderId, tvRequestId, tvBookingDate, tvBookedFor, tvOrderStatus;
        private Button btnAccept, btnReject, btnCompleteOrder, btnFoodieCancelOrder;
        private RelativeLayout rlHCAcceptReject, rlHcCompleteOrder, rlFoodieCancelOrder;

        MyOrdersViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number);
            tvEmailId = itemView.findViewById(R.id.tv_emailId);
            tvOrderType = itemView.findViewById(R.id.tv_order_type);
            tvNoOfGuest = itemView.findViewById(R.id.tv_no_of_guest);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOrderTiming = itemView.findViewById(R.id.tv_order_timing);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvRequestId = itemView.findViewById(R.id.tv_request_id);
            tvBookingDate = itemView.findViewById(R.id.tv_booking_date);
            tvBookedFor = itemView.findViewById(R.id.tv_booked_for);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);

            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);
            ivCall = itemView.findViewById(R.id.iv_call);
            ivMessage = itemView.findViewById(R.id.iv_message);
            ivShowDirections = itemView.findViewById(R.id.iv_show_directions);
            ivGiveReview = itemView.findViewById(R.id.iv_give_review);

            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
            btnCompleteOrder = itemView.findViewById(R.id.btn_complete_order);
            btnFoodieCancelOrder = itemView.findViewById(R.id.btn_foodie_cancel_order);

            rlHCAcceptReject = itemView.findViewById(R.id.rl_hc_accept_reject_order);
            rlHcCompleteOrder = itemView.findViewById(R.id.rl_hc_complete_order);
            rlFoodieCancelOrder = itemView.findViewById(R.id.rl_foodie_cancel_order);

            ivProfilePic.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
            btnReject.setOnClickListener(this);
            ivCall.setOnClickListener(this);
            ivMessage.setOnClickListener(this);
            ivShowDirections.setOnClickListener(this);
            ivGiveReview.setOnClickListener(this);
            btnCompleteOrder.setOnClickListener(this);
            btnFoodieCancelOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    ServiceUtils.foodieOrderAcceptReject(context, userModel.getUserId(), homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderRequestId(),
                            Constants.OrderActionType.HC_ACCEPTED_ORDER, "", new ServiceUtils.OrderActionInterface() {
                                @Override
                                public void onOrderAction(BaseModel baseModel) {
                                    if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                        DialogUtils.showAlert(context, "Order Accepted Successfully", new Runnable() {
                                            @Override
                                            public void run() {
                                                homeChefIncomingOrderModelArrayList.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                            }
                                        });
                                    } else {
                                        DialogUtils.showAlert(context, baseModel.getStatusMessage());
                                    }
                                }
                            });
                    break;
                case R.id.btn_reject:
                    ServiceUtils.foodieOrderAcceptReject(context, userModel.getUserId(), homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderRequestId(),
                            Constants.OrderActionType.HC_REJECTED_ORDER, "", new ServiceUtils.OrderActionInterface() {
                                @Override
                                public void onOrderAction(BaseModel baseModel) {
                                    if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                        DialogUtils.showAlert(context, "Order Rejected Successfully", new Runnable() {
                                            @Override
                                            public void run() {
                                                homeChefIncomingOrderModelArrayList.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                            }
                                        });
                                    } else {
                                        DialogUtils.showAlert(context, baseModel.getStatusMessage());
                                    }

                                }
                            });
                    break;
                case R.id.iv_call:
                    makeCall(homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getFoodieMobileNumber());
                    break;
                case R.id.iv_message:
                    message();
                    break;
                case R.id.iv_show_directions:
                    UserLocation userLocation = SharedPreference.getUserLocation(context);
                    double destLatitude = 28.644800;
                    double destLongitude = 77.216721;
                    Utils.showDirections(context, userLocation.getLatitude(), userLocation.getLongitude(), destLatitude, destLongitude);
                    break;
                case R.id.iv_give_review:
                    DialogUtils.showRatingDialog(context, new ReviewSubmitInterface() {
                        @Override
                        public void onReviewSubmit(int rating, String reviewDesc) {
                            System.out.println("Rating:-" + rating);
                            System.out.println("Review Desc:-" + reviewDesc);
                            ServiceUtils.submitReview(context, userModel.getUserId(), homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getFoodiesUserId(), rating, homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderId(), reviewDesc);
                        }
                    });
                    break;
                case R.id.iv_profile_pic:
                    if (userModel.getAccountType().equals(Constants.Role.FOODIE.getStringRole()))
                        Utils.showProfile(context, homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getFoodiesUserId());
                    break;
                case R.id.btn_complete_order:
                    DialogUtils.showOrderOtpDialog(context, new OtpSubmitInterface() {
                        @Override
                        public void onOtpEnter(String otp) {
                            System.out.println("Otp is:-" + otp);
                            ServiceUtils.foodieOrderAcceptReject(context, userModel.getUserId(), homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).getOrderRequestId(),
                                    Constants.OrderActionType.HC_COMPLETED_ORDER, otp, new ServiceUtils.OrderActionInterface() {
                                        @Override
                                        public void onOrderAction(BaseModel baseModel) {
                                            if (baseModel.getStatusCode() == Constants.ServerResponseCode.SUCCESS) {
                                                DialogUtils.showAlert(context, "Order Completed Successfully", new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        homeChefIncomingOrderModelArrayList.get(getAdapterPosition()).setRequestStatus(Constants.OrderActionType.HC_COMPLETED_ORDER);
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                            } else {
                                                DialogUtils.showAlert(context, baseModel.getStatusMessage());
                                            }
                                        }
                                    });
                        }
                    });
                    break;
                case R.id.btn_foodie_cancel_order:
                    foodieCancelOrder(getAdapterPosition());
                    break;
            }
        }
    }

    class TitleSeparatorViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        TitleSeparatorViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

}
