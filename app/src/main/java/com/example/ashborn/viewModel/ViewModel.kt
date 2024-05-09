package com.example.ashborn.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AshbornViewModel(): ViewModel() {
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification

    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification


    var userName by mutableStateOf("")
        private set // Optional: restrict external modification

    fun setPinX(pin: String) {
        this.pin = pin
    }

    fun setUserNameX(userName: String) {
        this.userName = userName
    }

    fun checkPin(): Boolean {
        return false
    }

    fun incrementWrongAttempts() {
        this.wrongAttempts++
    }


}