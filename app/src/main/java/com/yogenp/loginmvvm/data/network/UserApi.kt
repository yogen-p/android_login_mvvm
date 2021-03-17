package com.yogenp.loginmvvm.data.network

import com.yogenp.loginmvvm.data.responses.LoginResponse
import retrofit2.http.GET

interface UserApi {

    @GET("user")
    suspend fun getUser(): LoginResponse
}