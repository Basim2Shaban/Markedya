package com.markedya.basim.Models;

public class SpinnerModel {
    private String place ;
    private int placeDeliveryPrice ;


    public SpinnerModel() {
    }

    public SpinnerModel(String place, int placeDeliveryPrice) {
        this.place = place;
        this.placeDeliveryPrice = placeDeliveryPrice;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPlaceDeliveryPrice() {
        return placeDeliveryPrice;
    }

    public void setPlaceDeliveryPrice(int placeDeliveryPrice) {
        this.placeDeliveryPrice = placeDeliveryPrice;
    }
}
