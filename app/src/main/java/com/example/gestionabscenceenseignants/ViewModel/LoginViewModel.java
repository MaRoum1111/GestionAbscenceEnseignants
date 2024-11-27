package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.LoginRepository;
import com.example.gestionabscenceenseignants.model.User;

public class LoginViewModel extends ViewModel {
    private final LoginRepository loginRepository;
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();

    public LoginViewModel() {
        loginRepository = new LoginRepository();
    }

    public LiveData<User> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        loginRepository.login(email, password, new LoginRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                loginResult.setValue(user);  // Mise à jour du résultat
            }

            @Override
            public void onFailure(String errorMessage) {
                loginResult.setValue(null);  // Échec de l'authentification
            }
        });
    }
}
