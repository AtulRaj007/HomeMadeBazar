package com.munchmash.shopping;

import android.content.Context;

import com.munchmash.model.MarketPlaceProductModel;
import com.munchmash.util.SharedPreference;

import java.util.ArrayList;

/**
 * Created by atulraj on 18/2/18.
 */

public class MarketPlaceShoppingCart {
    public static ArrayList<MarketPlaceProductModel> marketPlaceProductModelArrayList;
    private Context context;

    private MarketPlaceShoppingCart(Context context) {
        this.context = context;
        marketPlaceProductModelArrayList = SharedPreference.getShoppingCart(context);
    }

    public synchronized static MarketPlaceShoppingCart getInstance(Context context) {
        return new MarketPlaceShoppingCart(context);
    }

    public static void addProductToCart(Context context, MarketPlaceProductModel marketPlaceProductModel) {
        marketPlaceProductModelArrayList.add(marketPlaceProductModel);
        SharedPreference.saveShoppingCart(context, marketPlaceProductModelArrayList);
    }

    public static ArrayList<MarketPlaceProductModel> getProductFromCart() {
        return marketPlaceProductModelArrayList;
    }

    public synchronized static void removeProductToCart(Context context, MarketPlaceProductModel marketPlaceProductModel) {
        for (MarketPlaceProductModel tempProductModel : marketPlaceProductModelArrayList) {
            if (tempProductModel.equals(marketPlaceProductModel)) {
                marketPlaceProductModelArrayList.remove(tempProductModel);
                break;
            }
        }
        SharedPreference.saveShoppingCart(context, marketPlaceProductModelArrayList);
    }

    public static void removeAllProductsFromCart(Context context) {
        try {
            SharedPreference.clearShoppingCart(context);
            marketPlaceProductModelArrayList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
