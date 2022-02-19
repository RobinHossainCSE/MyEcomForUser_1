package com.example.myecomforuser.repos;

import android.util.Log;


import com.example.myecomforuser.callback.OnCartItemQueryCompleteListener;
import com.example.myecomforuser.callback.OnCategoryQueryCompleteListener;
import com.example.myecomforuser.callback.OnCheckUserListener;
import com.example.myecomforuser.callback.OnProductQueryCompleteListener;
import com.example.myecomforuser.callback.OnSingleProductQueryCompleteListener;
import com.example.myecomforuser.models.CartModel;
import com.example.myecomforuser.models.ProductModel;
import com.example.myecomforuser.models.EcomUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductRepository {
    private static final String TAG = ProductRepository.class.getSimpleName();

    private static final String COLLECTION_CATEGORY = "Categories";
    private static final String COLLECTION_PRODUCT = "Products";
    public static final String COLLECTION_USER = "Users";
    public static final String COLLECTION_CART = "Cart List";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addToCart(CartModel cartModel, String uid){
        db.collection(COLLECTION_USER)
                .document(uid)
                .collection(COLLECTION_CART)
                .document(cartModel.getProductId())
                .set(cartModel)
                .addOnSuccessListener(unused -> {

                }).addOnFailureListener(unused ->{

        });
    }
    public void removeFromCart(String uid, String productId){
        db.collection(COLLECTION_USER)
                .document(uid)
                .collection(COLLECTION_CART)
                .document(productId)
                .delete()
                .addOnSuccessListener(unused -> {

                }).addOnFailureListener(unused ->{

        });
    }



    public void getAllCategories(OnCategoryQueryCompleteListener listener){
        db.collection(COLLECTION_CATEGORY).addSnapshotListener((value, error) -> {
            if(error != null) return;
            final List<String> temp = new ArrayList<>();
            for(DocumentSnapshot doc: value.getDocuments()){
                temp.add(doc.get("name", String.class));
            }
            Collections.sort(temp);
            listener.onCategoryQueryComplete(temp);
        });
    }

    public void getAllProducts(OnProductQueryCompleteListener listener){
        db.collection(COLLECTION_PRODUCT).addSnapshotListener((value, error) -> {
            if(error != null) return;
            final List<ProductModel> temp = new ArrayList<>();
            for(DocumentSnapshot doc: value.getDocuments()){
                temp.add(doc.toObject(ProductModel.class));
            }
            listener.onProductQueryComplete(temp);
        });
    }

    public void getProductByProductId(String productId, OnSingleProductQueryCompleteListener listener){
        db.collection(COLLECTION_PRODUCT).document(productId)
                .addSnapshotListener((value, error) -> {
                    if(error != null) return;
                    final ProductModel model = value.toObject(ProductModel.class);
                    listener.onSingleProductQueryComplete(model);

        });
    }

    public void getAllProductsByCategory(String category, OnProductQueryCompleteListener listener){
        db.collection(COLLECTION_PRODUCT)
                .whereEqualTo("category", category)
                .addSnapshotListener((value, error) -> {
            if(error != null) return;
            final List<ProductModel> temp = new ArrayList<>();
            for(DocumentSnapshot doc: value.getDocuments()){
                temp.add(doc.toObject(ProductModel.class));
            }
            listener.onProductQueryComplete(temp);
        });
    }




    public void checkUser(String uid, OnCheckUserListener listener) {
        db.collection(COLLECTION_USER)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().exists()){
                        listener.onCheckUser(true);

                    }else{
                        listener.onCheckUser(false);
                    }
                });
    }
    public void getAllCartItems(String uid, OnCartItemQueryCompleteListener listener){
        db.collection(COLLECTION_USER)
                .document(uid)
                .collection(COLLECTION_CART)
                .addSnapshotListener((value, error) -> {
                    if(error!= null) return;
                    List<CartModel> items = new ArrayList<>();
                    for(DocumentSnapshot doc: value.getDocuments()){
                        items.add(doc.toObject(CartModel.class));
                    }
                    listener.onCartItemQueryCompleted(items);
                });
    }
}
