package com.homemadebazar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homemadebazar.R;
import com.homemadebazar.activity.HomeShopViewActivity;
import com.homemadebazar.model.HomeChiefNearByModel;
import com.homemadebazar.util.CircleImageView;
import com.homemadebazar.util.Constants;
import com.homemadebazar.util.Utils;

import java.util.ArrayList;


/**
 * Created by atulraj on 22/11/17.
 */

public class FoodieHomeMapFragment extends BaseFragment implements OnMapReadyCallback {
    private SupportMapFragment supportMapFragment;
    private ArrayList<HomeChiefNearByModel> dataList = new ArrayList<>();
    ;
    private GoogleMap mGoogleMap;
    private LatLng zoomMarkerLocation;
    private RelativeLayout memberInfoLayout;
    private CircleImageView profileImageView;
    private TextView tvName, tvAddress, tvDistance, tvShopName;
    private String tempPosition = "";
    private HomeChiefNearByModel homeChiefNearByModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foodie_home_map, container, false);
    }

    @Override
    protected void initUI() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        memberInfoLayout = getView().findViewById(R.id.member_info_ll);
        profileImageView = getView().findViewById(R.id.iv_profile_pic);
        tvName = getView().findViewById(R.id.tv_name);
        tvShopName = getView().findViewById(R.id.tv_shop_name);
        tvAddress = getView().findViewById(R.id.tv_address);
        tvDistance = getView().findViewById(R.id.tv_distance);
    }

    @Override
    protected void initialiseListener() {

        memberInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeShopViewActivity.getIntent(getActivity(), homeChiefNearByModel));
            }
        });
    }

    @Override
    protected void setData() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (Utils.checkLocationPermission(getActivity())) {
            setMarkerData();
        }
    }

    private void setMarkerData() {
        for (int i = 0; i < dataList.size(); i++) {
            drawMarker(new LatLng(Double.parseDouble(dataList.get(i).getLatitude()), Double.parseDouble(dataList.get(i).getLongitude())), dataList.get(i).getFirstName() + " " + dataList.get(i).getLastName(), dataList.get(i).getRating(), i);
            zoomMarkerLocation = new LatLng(Double.parseDouble(dataList.get(0).getLatitude()), Double.parseDouble(dataList.get(0).getLongitude()));
        }
        if (zoomMarkerLocation != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(zoomMarkerLocation));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }


    public void setFoodieMapDataList(ArrayList<HomeChiefNearByModel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        setMarkerData();
    }

    private void drawMarker(LatLng point, String name, String rating, int position) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(name);
        markerOptions.snippet(position + "");
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity(), rating);
        mGoogleMap.setInfoWindowAdapter(adapter);
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getActivity(),"Hello "+marker.getSnippet(),Toast.LENGTH_SHORT).show();
                callMemberData(marker.getSnippet());
            }
        });
    }

    private void callMemberData(String position) {
        int pos = Integer.parseInt(position);
        if (memberInfoLayout.getVisibility() == View.VISIBLE && tempPosition.equals(position)) {
            memberInfoLayout.setVisibility(View.GONE);
        } else {
            memberInfoLayout.setVisibility(View.VISIBLE);
            tempPosition = position;
            homeChiefNearByModel = dataList.get(pos);
            if (!TextUtils.isEmpty(homeChiefNearByModel.getProfileImage())) {
                Glide.with(getActivity()).load(homeChiefNearByModel.getProfileImage()).into(profileImageView);
            }
            tvName.setText(homeChiefNearByModel.getFirstName() + " " + homeChiefNearByModel.getLastName());
            tvShopName.setText(homeChiefNearByModel.getShopName());
            tvAddress.setText(homeChiefNearByModel.getAddress());
//            tvDistance.setText(String.format("%.2f", homeChiefNearByModel.getDistance()) + " Meter Away");
            tvDistance.setText(homeChiefNearByModel.getDistance() + " Meter Away");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.Keys.LOCATION_REQUEST) {
            setMarkerData();
        }
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private Activity context;
        private String snippet;

        public CustomInfoWindowAdapter(Activity context, String snippet) {
            this.context = context;
            this.snippet = snippet;
        }

        @Override
        public View getInfoWindow(Marker marker) {
           /* View view = context.getLayoutInflater().inflate(R.layout.custom_marker_infow_window, null);

            TextView tvTitle = (TextView) view.findViewById(R.id.tv_marker_name);
            TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_marker_rating);

            tvTitle.setText(marker.getTitle());
            tvSubTitle.setText(marker.getSnippet());

            return view;*/
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = context.getLayoutInflater().inflate(R.layout.custom_marker_infow_window, null);

            TextView tvName = (TextView) view.findViewById(R.id.tv_marker_name);
            TextView tvRating = (TextView) view.findViewById(R.id.tv_marker_rating);

            tvName.setText(marker.getTitle());
//            tvSubTitle.setText(marker.getSnippet());
            tvRating.setText(snippet);

            return view;
//           return null;
        }
    }
}
