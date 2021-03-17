package com.yogenp.loginmvvm.data.repository

import com.yogenp.loginmvvm.data.network.Resource
import com.yogenp.loginmvvm.data.network.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T>{
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun logout(api: UserApi) = withContext(Dispatchers.IO) {
        api.logout()
    }
}