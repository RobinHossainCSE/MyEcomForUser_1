package com.example.myecomforuser.models;

public class UserProductModel extends ProductModel{
    private boolean isInCart;
    private boolean isInFavorite;

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public boolean isInFavorite() {
        return isInFavorite;
    }

    public void setInFavorite(boolean inFavorite) {
        isInFavorite = inFavorite;
    }
}
