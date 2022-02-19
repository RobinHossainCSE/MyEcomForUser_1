package com.example.myecomforuser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myecomforuser.databinding.FragmentUserLoginBinding;
import com.example.myecomforuser.utils.HelperFunction;
import com.example.myecomforuser.viewmodels.LoginViewModel;

public class UserLoginFragment extends Fragment {
    private FragmentUserLoginBinding binding;
    private LoginViewModel loginViewModel;
    private boolean isLogin;




    public UserLoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLoginBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);

        binding.loginBtn.setOnClickListener(v -> {
            isLogin = true;
            authenticate();
        });
        binding.registerBtn.setOnClickListener(v -> {
            isLogin = false;
            authenticate();
        });

        loginViewModel.getStateLiveData()
                .observe(getViewLifecycleOwner(), authState -> {
                    if (authState == LoginViewModel.AuthState.AUTHENTICATED) {
                        Navigation.findNavController(container)
                                .navigate(R.id.action_userLoginFragment_to_productListFragment);
                    }
                });

        loginViewModel.getErrMsgLiveData()
                .observe(getViewLifecycleOwner(), errMsg -> {
                    binding.errMsgTV.setText(errMsg);
                });

        binding.loginBtn.setOnClickListener(view -> {
            isLogin = true;
            authenticate();

        });
        binding.registerBtn.setOnClickListener(view -> {

            isLogin = false;
            authenticate();

        });


        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.emailInputET.getText().toString();
        final String password = binding.passwordInputET.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            HelperFunction.showToast(getActivity(), "Please provide both field values");
            return;
        }
        if (isLogin) {
            loginViewModel.login(email, password);
        }else {
            loginViewModel.register(email, password);
        }
    }

}