package com.homemadebazar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.homemadebazar.R;
import com.homemadebazar.model.RatingModel;

/**
 * Created by Atul on 3/23/18.
 */

public class UserRatingFragment extends BaseFragment {
    private TextView tvName, tvRating, tvDescription;
    private ImageView ivProfilePic;
    private RatingModel ratingModel;
    private static String KEY_RATING_MODEL = "KEY_RATING_MODEL";

    @Override
    protected void initUI() {
        ratingModel = (RatingModel) getArguments().getSerializable(KEY_RATING_MODEL);
        tvName = getView().findViewById(R.id.tv_name);
        tvRating = getView().findViewById(R.id.tv_rating);
        tvDescription = getView().findViewById(R.id.tv_description);
        ivProfilePic = getView().findViewById(R.id.iv_profile_pic);
    }

    public static UserRatingFragment instantiateFragment(RatingModel ratingModel) {
        UserRatingFragment userRatingFragment = new UserRatingFragment();
        try {
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_RATING_MODEL, ratingModel);
            userRatingFragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRatingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_rating, container, false);
    }

    @Override
    protected void initialiseListener() {

    }

    @Override
    protected void setData() {
        try {
            if (ratingModel != null) {
                tvName.setText(ratingModel.getFirstName() + " " + ratingModel.getLastName());
                tvRating.setText(ratingModel.getRating().trim());
                tvDescription.setText(ratingModel.getDescription());

                if (!TextUtils.isEmpty(ratingModel.getDpUrl()))
                    Glide.with(getActivity()).load(ratingModel.getDpUrl()).into(ivProfilePic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
