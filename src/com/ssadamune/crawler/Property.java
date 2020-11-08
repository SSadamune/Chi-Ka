package com.ssadamune.crawler;

import java.util.HashSet;

public class Property {
    protected int id;
    protected int areaCode;
    protected int todofukenCode;
    protected int shikugunCode;
    protected int price10K;
    protected String madori;
    protected float totalArea;
    //完成時期(築年月) e.g. 197610
    protected String completeDate;
    //入居時期(年月) 即入居可は 000000
    protected String moveInDate;
    protected byte direction;

    protected HashSet<String> features = new HashSet<>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }
    public int getTodofukenCode() {
        return todofukenCode;
    }
    public void setTodofukenCode(int todofukenCode) {
        this.todofukenCode = todofukenCode;
    }
    public int getShikugunCode() {
        return shikugunCode;
    }
    public void setShikugunCode(int shikugunCode) {
        this.shikugunCode = shikugunCode;
    }
    public int getPrice() {
        return price10K;
    }
    public void setPrice(int price) {
        this.price10K = price;
    }
    public String getMadori() {
        return madori;
    }
    public void setMadori(String madori) {
        this.madori = madori;
    }
    public float getTotalArea() {
        return totalArea;
    }
    public void setTotalArea(float totalArea) {
        this.totalArea = totalArea;
    }
    public String getCompleteDate() {
        return completeDate;
    }
    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
    public String getMoveInDate() {
        return moveInDate;
    }
    public void setMoveInDate(String moveInDate) {
        this.moveInDate = moveInDate;
    }
    public byte getDirection() {
        return direction;
    }
    public void setDirection(byte direction) {
        this.direction = direction;
    }
    public void addFeature(String featureCode) {
        this.features.add(featureCode);
    }

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
