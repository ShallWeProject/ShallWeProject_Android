package com.shall_we.login.data

interface IAuthSignOut {
    fun onPostAuthSignOutSuccess(response: AuthSignOutResponse)
    fun onPostAuthSignOutFailure(message: String)
}