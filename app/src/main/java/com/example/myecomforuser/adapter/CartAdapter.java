package com.example.myecomforuser.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myecomforuser.callback.OnCartItemQuantityChangeListener;

import com.example.myecomforuser.databinding.CartItemRowBinding;
import com.example.myecomforuser.databinding.ItemListRowBinding;
import com.example.myecomforuser.models.CartModel;


public class CartAdapter extends ListAdapter<CartModel, CartAdapter.ProductViewHolder> {
    private OnCartItemQuantityChangeListener quantityChangeListener;


    public CartAdapter(OnCartItemQuantityChangeListener quantityChangeListener) {

        super(new ProductDiff());
        this.quantityChangeListener = quantityChangeListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CartItemRowBinding binding = CartItemRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final CartModel cartModel = getItem(position);
        holder.bind(cartModel);




    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private CartItemRowBinding binding;
        public ProductViewHolder(CartItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
        public void bind(CartModel cartModel) {
            binding.setCart(cartModel);

        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<CartModel>{
        @Override
        public boolean areItemsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }

}