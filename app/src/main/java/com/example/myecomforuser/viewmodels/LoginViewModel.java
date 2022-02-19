package com.example.myecomforuser.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myecomforuser.callback.OnCheckUserListener;
import com.example.myecomforuser.models.EcomUser;
import com.example.myecomforuser.repos.ProductRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {
    private ProductRepository productRepository = new ProductRepository();
    final String TAG = LoginViewModel.class.getSimpleName();
    public enum AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }
    private MutableLiveData<AuthState> stateLiveData;
    private MutableLiveData<String> errMsgLiveData;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public LoginViewModel() {
        stateLiveData = new MutableLiveData<>();
        errMsgLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            stateLiveData.postValue(AuthState.UNAUTHENTICATED);
        }else {
            stateLiveData.postValue(AuthState.AUTHENTICATED);
        }
    }

    public LiveData<AuthState> getStateLiveData() {
        return stateLiveData;
    }

    public LiveData<String> getErrMsgLiveData() {
        return errMsgLiveData;
    }

    public FirebaseUser getUser() {
        return user;
    }



    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    productRepository.checkUser(authResult.getUser().getUid(), new OnCheckUserListener() {
                        @Override
                        public void onCheckUser(boolean status) {
                            if(status){
                                user = authResult.getUser();
                                stateLiveData.postValue(AuthState.AUTHENTICATED);
                            }else {
                                auth.signOut();
                                errMsgLiveData.postValue("This email is not register as Admin, Please use a valid account!");
                            }


                        }
                    });
                }).addOnFailureListener(e -> {
                    errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }


    public void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    user = authResult.getUser();
                    stateLiveData.postValue(AuthState.AUTHENTICATED);
                    addUserToDatabase();
                }).addOnFailureListener(e -> {
            errMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }



    public void logout() {
        if (user != null) {
            auth.signOut();
            stateLiveData.postValue(AuthState.UNAUTHENTICATED);
        }
    }

    public boolean isEmailVerified() {
        return user.isEmailVerified();
    }

    public void sendVerificationMail() {
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void addUserToDatabase(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection(ProductRepository.COLLECTION_USER)
                .document(user.getUid());
        final EcomUser ecomUser = new EcomUser(user.getUid(), null, user.getEmail(), null);
        doc.set(ecomUser).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {
            Log.e(TAG, "addUserToDatabase" +e.getLocalizedMessage());
        });
    }

}
