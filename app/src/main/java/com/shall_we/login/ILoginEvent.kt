package com.shall_we.login

interface ILoginEvent {
    fun onLoginSuccess()
    fun onLoginFailed(message: String)
}