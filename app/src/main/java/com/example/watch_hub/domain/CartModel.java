package com.example.watch_hub.domain;

public class CartModel {
    private String orderNumber;
    private String cartId;
    private String productName;
    private String productImage;
    private String productPrice;
    private String productQuantity;
    private String sellerUid;
    public boolean is_selected;
    public CartModel() {
    }
    public CartModel(String orderNumber,String cartId, String productName, String productImage, String productPrice, String productQuantity, String sellerUid) {
        this.orderNumber = orderNumber;
        this.cartId = cartId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.sellerUid = sellerUid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getSellerUid() {
        return sellerUid;
    }
}
