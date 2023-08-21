package com.shall_we.login.data

interface IAuthSign {
    fun onPostAuthSignInSuccess(response: AuthResponse)
    fun onPostAuthSignInFailed(message: String)
    fun onPostAuthSignUpSuccess(response: AuthResponse)
    fun onPostAuthSignUpFailed(message: String)
}