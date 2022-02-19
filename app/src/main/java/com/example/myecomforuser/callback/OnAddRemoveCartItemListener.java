package com.example.myecomforuser.callback;

import com.example.myecomforuser.models.CartModel;

public interface OnAddRemoveCartItemListener {
    void onAddCartItem(CartModel cartModel, int position);
    void onRemoveCartItem(String productId, int position);
}
