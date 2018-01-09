package com.homemadebazar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.homemadebazar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by atulraj on 9/12/17.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private ArrayList<String> imageArrayList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ImagePagerAdapter(Context context, ArrayList<String> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = view.findViewById(R.id.iv_image);
        Picasso.with(context).load(imageArrayList.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
