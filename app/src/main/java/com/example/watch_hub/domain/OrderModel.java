package com.example.watch_hub.domain;

public class OrderModel {
private String orderNumber;
private String customerName;
private String customerAddress;
private String country;
private String number;
private String itemExpense;
private String deliveryCharge;
private String orderTrackingNumber;
private String courier;
private String orderPlaceDate;
private String orderStatus;

private String uid;

    public OrderModel() {
    }

    public OrderModel(String orderNumber, String customerName, String customerAddress, String country, String number, String itemExpense, String deliveryCharge, String orderTrackingNumber, String courier, String orderPlaceDate, String orderStatus, String uid) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.country = country;
        this.number = number;
        this.itemExpense = itemExpense;
        this.deliveryCharge = deliveryCharge;
        this.orderTrackingNumber = orderTrackingNumber;
        this.courier = courier;
        this.orderPlaceDate = orderPlaceDate;
        this.orderStatus = orderStatus;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getItemExpense() {
        return itemExpense;
    }

    public void setItemExpense(String itemExpense) {
        this.itemExpense = itemExpense;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    public void setOrderTrackingNumber(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getOrderPlaceDate() {
        return orderPlaceDate;
    }

    public void setOrderPlaceDate(String orderPlaceDate) {
        this.orderPlaceDate = orderPlaceDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
