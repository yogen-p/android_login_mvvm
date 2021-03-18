package com.yogenp.loginmvvm.data.repository

import com.yogenp.loginmvvm.data.network.UserApi

class UserRepository(
    private val api: UserApi
) : BaseRepository() {

    suspend fun getUser() = safeApiCall {
        api.getUser()
    }
}