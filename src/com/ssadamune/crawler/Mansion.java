package com.ssadamune.crawler;

public class Mansion extends Property{
    // TODO



    @Override
    public String toString() {
        return "物件 { ID：" + this.id
                + ", 地域コード：" + this.areaCode
                + ", 都道府県コード：" + this.todofukenCode
                + ", 市区郡コード：" + this.shikugunCode
                + ", 価格：" + this.price10K + "万円"
                + ", 間取り：" + this.madori
                + ", 専用面積：" + this.totalArea
                + ", 完成時期：" + this.completeDate
                + ", 入居時期：" + this.moveInDate
                + ", 向き：" + this.direction
                + " }\n"
                + "特徴 " + features
                + "\n";
    }

}
