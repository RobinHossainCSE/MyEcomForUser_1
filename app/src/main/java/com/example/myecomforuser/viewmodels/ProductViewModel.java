package com.example.myecomforuser.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myecomforuser.callback.OnCartItemQueryCompleteListener;
import com.example.myecomforuser.models.CartModel;
import com.example.myecomforuser.models.ProductModel;
import com.example.myecomforuser.models.EcomUser;
import com.example.myecomforuser.models.UserProductModel;
import com.example.myecomforuser.repos.ProductRepository;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final String TAG = ProductViewModel.class.getSimpleName();
    private final ProductRepository repository = new ProductRepository();
    public MutableLiveData<List<String>> categoryListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<ProductModel>> productListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<UserProductModel>> userProductListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CartModel>> cartListLiveData = new MutableLiveData<>();
    public MutableLiveData<ProductModel> productLiveData = new MutableLiveData<>();

    public ProductViewModel() {
        getCategories();
        getProducts();
    }

    public void addToCart(CartModel cartModel, String uid){
        repository.addToCart(cartModel, uid);
    }

    public void removeFromCart(String uid, String productId){
        repository.removeFromCart(uid, productId);
    }

    private void getProducts() {
        repository.getAllProducts(items -> {
            productListLiveData.postValue(items);
        });
    }


    private void getCategories(){
        repository.getAllCategories(items -> categoryListLiveData.postValue(items));

    }

    private void getProductsByCategory(String category){
        repository.getAllProductsByCategory(category,items -> productListLiveData.postValue(items));
    }

    public void getProductById(String productId){
        repository.getProductByProductId(productId, productLiveData::postValue);
    }

    public void getAllCartItems(String uid){
        repository.getAllCartItems(uid, new OnCartItemQueryCompleteListener() {
            @Override
            public void onCartItemQueryCompleted(List<CartModel> cartModels) {
                cartListLiveData.postValue(cartModels);

            }
        });
    }

    public void prepareUserProductList(){
        final List<UserProductModel> upmList = new ArrayList<>();
        for(ProductModel p : productListLiveData.getValue()){
            final UserProductModel upm = new UserProductModel();
            upm.setProductId(p.getProductId());
            upm.setProductName(p.getProductName());
            upm.setCategory(p.getCategory());
            upm.setDescription(p.getDescription());
            upm.setPrice(p.getPrice());
            upm.setProductImageUrl(p.getProductImageUrl());
            upm.setInCart(false);
            upm.setInFavorite(false);
            upmList.add(upm);
        }
        for(CartModel c : cartListLiveData.getValue()){
            for (UserProductModel u : upmList){
                if (c.getProductId().equals(u.getProductId())){
                    u.setInCart(true);
                }
            }
        }
        userProductListLiveData.postValue(upmList);
    }




}
