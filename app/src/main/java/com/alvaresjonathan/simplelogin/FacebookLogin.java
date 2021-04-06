package com.alvaresjonathan.simplelogin;

import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLogin {
    public static CallbackManager callbackManager;
    public static void signIn(LoginButton loginButton) {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Demo", "Login Successful");
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("MainActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("MainActivity", exception.getCause().toString());
            }
        });
    }
}
