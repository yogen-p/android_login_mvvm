package com.yogenp.loginmvvm.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogenp.loginmvvm.data.network.Resource
import com.yogenp.loginmvvm.data.repository.AuthRepository
import com.yogenp.loginmvvm.data.responses.LoginResponse
import com.yogenp.loginmvvm.data.responses.RegisterResponse
import com.yogenp.loginmvvm.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository) {

    private val _loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _registerResponse : MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()
    val registerResponse: LiveData<Resource<RegisterResponse>>
        get() = _registerResponse


    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }

    fun registerUser(
        name: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        _registerResponse.value = Resource.Loading
        _registerResponse.value = repository.register(name, email, password)
    }

    suspend fun saveAuthToken(token: String){
        repository.saveAuthToken(token)
    }

}