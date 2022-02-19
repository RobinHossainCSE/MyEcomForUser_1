package com.example.myecomforuser.callback;

import com.example.myecomforuser.models.ProductModel;

import java.util.List;

public interface OnProductQueryCompleteListener {
    void onProductQueryComplete(List<ProductModel> items);
}
