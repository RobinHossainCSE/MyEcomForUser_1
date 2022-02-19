package com.example.myecomforuser.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class CustomBindingAdapter {
    @BindingAdapter(value = "app:setIcon")
    public static void setIcon(ImageView imageView, int icon){
        imageView.setImageResource(icon);
    }

    @BindingAdapter(value = "app:setImageUrl")
    public static void setImageUrl(ImageView imageView, String url){
        if(url != null){
            Picasso.get().load(url).into(imageView);
        }
    }
}
