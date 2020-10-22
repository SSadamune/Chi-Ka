package com.sadaki.modular;

import com.google.gson.annotations.SerializedName;

public class Estate {
    private int id;
    @SerializedName("areaCd")
    private int areaCode;
    @SerializedName("todofukenCd")
    private int todofukenCode;
    @SerializedName("shikugunCd")
    private int shikugunCode;
    @SerializedName("headerKakakuDisp")
    private int[] price;

    public void setId(int estateId) {
        this.id = estateId;
    }

    @Override
    public String toString() {
        return "Estate [物件ID：" + this.id
                + ", 区域コード：" + areaCode
                + ", 都道府県コード：" + todofukenCode
                + ", 市区郡コード：" + shikugunCode
                + ", 価格：" + price[0]
                + "]";
    }

}
