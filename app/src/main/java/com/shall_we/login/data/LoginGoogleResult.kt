package com.shall_we.login.data

sealed class LoginGoogleResult<out T> {
    object Loading : LoginGoogleResult<Nothing>()
    object UnLoading : LoginGoogleResult<Nothing>()
    data class Success<T>(val data: T) : LoginGoogleResult<T>()
    data class Unauthorized(val throwable: Throwable) : LoginGoogleResult<Nothing>()
    data class Error(val throwable: Throwable) : LoginGoogleResult<Nothing>()
}
