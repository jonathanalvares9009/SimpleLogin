package com.alvaresjonathan.simplelogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("login/")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);
}
