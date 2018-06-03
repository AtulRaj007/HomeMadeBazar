package com.munchmash.model;

/**
 * Created by atulraj on 26/12/17.
 */

public class MarketPlaceProductBrandModel {
    private String brandId;
    private String brandName;
    private String brandDesc;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return brandName;
    }
}
