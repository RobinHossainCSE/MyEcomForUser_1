package com.example.myecomforuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myecomforuser.adapter.CartAdapter;
import com.example.myecomforuser.callback.OnCartItemQuantityChangeListener;
import com.example.myecomforuser.databinding.FragmentCartListBinding;
import com.example.myecomforuser.viewmodels.LoginViewModel;
import com.example.myecomforuser.viewmodels.ProductViewModel;


public class CartListFragment extends Fragment {
    private FragmentCartListBinding binding;
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private CartAdapter adapter;


    public CartListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartListBinding.inflate(inflater);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
        adapter = new CartAdapter(new OnCartItemQuantityChangeListener() {
            @Override
            public void onCartItemQuantityChange() {

            }
        });


        return binding.getRoot();
    }
}