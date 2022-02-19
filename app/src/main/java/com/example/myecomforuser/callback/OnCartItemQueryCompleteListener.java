package com.example.myecomforuser.callback;

import com.example.myecomforuser.models.CartModel;

import java.util.List;

public interface OnCartItemQueryCompleteListener {
    void onCartItemQueryCompleted(List<CartModel> cartModels);
}
