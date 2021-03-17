package com.yogenp.loginmvvm.ui.base

import androidx.lifecycle.ViewModel
import com.yogenp.loginmvvm.data.network.UserApi
import com.yogenp.loginmvvm.data.repository.BaseRepository

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel(){

    suspend fun logout(api: UserApi) = repository.logout(api)
}