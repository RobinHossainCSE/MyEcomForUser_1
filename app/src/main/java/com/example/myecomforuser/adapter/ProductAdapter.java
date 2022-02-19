package com.example.myecomforuser.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myecomforuser.callback.OnAddRemoveCartItemListener;
import com.example.myecomforuser.callback.OnProductItemClickListener;
import com.example.myecomforuser.databinding.ItemListRowBinding;
import com.example.myecomforuser.models.CartModel;
import com.example.myecomforuser.models.ProductModel;
import com.example.myecomforuser.models.UserProductModel;

public class ProductAdapter extends ListAdapter<UserProductModel, ProductAdapter.ProductViewHolder> {
    private OnProductItemClickListener listener;
    private  OnAddRemoveCartItemListener cartItemListener;

    public ProductAdapter(OnProductItemClickListener listener, OnAddRemoveCartItemListener cartItemListener) {

        super(new ProductDiff());
        this.listener = listener;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemListRowBinding binding = ItemListRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final UserProductModel userProductModel = getItem(position);
        holder.bind(userProductModel);

        if(userProductModel.isInCart()){
            holder.binding.addToCartBtn.setVisibility(View.GONE);
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);

        }else {
            holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
            holder.binding.addToCartBtn.setVisibility(View.GONE);
        }
       holder.binding.addToCartBtn.setOnClickListener(v ->{
           holder.binding.addToCartBtn.setVisibility(View.GONE);
           holder.binding.removeToCartBtn.setVisibility(View.VISIBLE);


       final CartModel cartModel= new CartModel(userProductModel.getProductId(), userProductModel.getProductName(), userProductModel.getPrice(), 1);
       cartItemListener.onAddCartItem(cartModel, position);


        });
        holder.binding.removeToCartBtn.setOnClickListener(v ->{
        holder.binding.addToCartBtn.setVisibility(View.VISIBLE);
        holder.binding.removeToCartBtn.setVisibility(View.GONE);
        cartItemListener.onRemoveCartItem(userProductModel.getProductId(), position);

        });


    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemListRowBinding binding;
        public ProductViewHolder(ItemListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.detailsProductImageView.setOnClickListener(v->{
                listener.onProductItemClicked(getItem(getAdapterPosition()).getProductId());
            });
        }
        public void bind(ProductModel productModel) {
            binding.setProduct(productModel);
        }
    }

    static class ProductDiff extends DiffUtil.ItemCallback<UserProductModel>{
        @Override
        public boolean areItemsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }

}
