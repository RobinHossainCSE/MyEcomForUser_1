package com.example.myecomforuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myecomforuser.adapter.ProductAdapter;
import com.example.myecomforuser.callback.OnAddRemoveCartItemListener;
import com.example.myecomforuser.callback.OnProductItemClickListener;
import com.example.myecomforuser.databinding.FragmentProductListBinding;
import com.example.myecomforuser.models.CartModel;
import com.example.myecomforuser.models.ProductModel;
import com.example.myecomforuser.models.UserProductModel;
import com.example.myecomforuser.viewmodels.LoginViewModel;
import com.example.myecomforuser.viewmodels.ProductViewModel;

import java.util.List;


public class ProductListFragment extends Fragment {
    private FragmentProductListBinding binding;
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;

    public ProductListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        productViewModel=new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        loginViewModel.getStateLiveData()
                .observe(getViewLifecycleOwner(), authState -> {
                    if (authState == LoginViewModel.AuthState.UNAUTHENTICATED) {
                        Navigation.findNavController(container)
                                .navigate(R.id.action_productListFragment_to_userLoginFragment);
                    }
                });
        final ProductAdapter adapter = new ProductAdapter(productId -> {
            //TODO:

        }, new OnAddRemoveCartItemListener() {
            @Override
            public void onAddCartItem(CartModel cartModel, int position) {
                productViewModel.addToCart(cartModel, loginViewModel.getUser().getUid());

            }

            @Override
            public void onRemoveCartItem(String productId, int position) {
                productViewModel.removeFromCart(loginViewModel.getUser().getUid(), productId);

            }
        });

        final GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        binding.productRV.setLayoutManager(glm);
        binding.productRV.setAdapter(adapter);
        productViewModel.productListLiveData.observe(getViewLifecycleOwner(), productModelList -> {
            //adapter.submitList(productModelList);
            productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
        });

        productViewModel.cartListLiveData.observe(getViewLifecycleOwner(), new Observer<List<CartModel>>() {
            @Override
            public void onChanged(List<CartModel> cartModels) {
                productViewModel.prepareUserProductList();
            }
        });

        productViewModel.userProductListLiveData.observe(getViewLifecycleOwner(), new Observer<List<UserProductModel>>() {
            @Override
            public void onChanged(List<UserProductModel> userProductModels) {
                adapter.submitList(userProductModels);
            }
        });
        return binding.getRoot();
    }
}