package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

class CarteViewModel(application: Application): AndroidViewModel(application) {

}

@Suppress("UNCHECKED_CAST")
class CarteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarteViewModel::class.java)) {
            return CarteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}