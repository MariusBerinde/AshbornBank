package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

class AltroViewModel(application: Application): AndroidViewModel(application) {

}

@Suppress("UNCHECKED_CAST")
class AltroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AltroViewModel::class.java)) {
            return AltroViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}