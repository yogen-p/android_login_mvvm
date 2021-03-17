package com.yogenp.loginmvvm.data.repository

import com.yogenp.loginmvvm.data.UserPreferences
import com.yogenp.loginmvvm.data.network.AuthApi

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) = safeApiCall {
        api.register(name, email, password)
    }

    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }
}