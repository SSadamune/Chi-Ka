package com.ssadamune.modular;

public class Estate {
    private int id;
    private int areaCode;
    private int todofukenCode;
    private int shikugunCode;
    private int price;
    private String madori;
    private float totalArea;
    private String completeDate;//完成時期(築年月) e.g. 197610
    private String moveInDate;//入居時期(年月) 即入居可は 000000
    private byte direction;

    private boolean canMoveInNow;
    private boolean isReformed;
    private boolean havingSystemKitchen;
    private boolean havingBathroomDryer;
    private boolean isFlooringChanged;
    private boolean havingRenovation;
    private boolean havingElevator;

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
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
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

    public boolean canMoveInNow() {
        return canMoveInNow;
    }
    public void setCanMoveInNow() {
        this.canMoveInNow = true;
    }
    public boolean isReformed() {
        return isReformed;
    }
    public void setReformed() {
        this.isReformed = true;
    }
    public boolean isHavingSystemKitchen() {
        return havingSystemKitchen;
    }
    public void setHavingSystemKitchen() {
        this.havingSystemKitchen = true;
    }
    public boolean isHavingBathroomDryer() {
        return havingBathroomDryer;
    }
    public void setHavingBathroomDryer() {
        this.havingBathroomDryer = true;
    }
    public boolean isFlooringChanged() {
        return isFlooringChanged;
    }
    public void setFlooringChanged() {
        this.isFlooringChanged = true;
    }
    public boolean isHavingRenovation() {
        return havingRenovation;
    }
    public void setHavingRenovation() {
        this.havingRenovation = true;
    }
    public boolean isHavingElevator() {
        return havingElevator;
    }
    public void setHavingElevator() {
        this.havingElevator = true;
    }

    @Override
    public String toString() {
        return "Estate [ 物件ID：" + this.id
                + ", 地域コード：" + areaCode
                + ", 都道府県コード：" + todofukenCode
                + ", 市区郡コード：" + shikugunCode
                + ", 価格：" + price
                + ", 間取り：" + madori
                + ", 専用面積：" + totalArea
                + ", 完成時期：" + completeDate
                + ", 向き：" + direction
                + " ]\n"
                + "[ 即入居可：" + canMoveInNow
                + ", 内装リフォーム：" + isReformed
                + ", システムキッチン：" + havingSystemKitchen
                + ", 浴室乾燥機：" + havingBathroomDryer
                + ", フローリング張替：" + isFlooringChanged
                + ", リノベーション：" + havingRenovation
                + ", エレベーター：" + havingElevator
                + " ]";
    }

}
