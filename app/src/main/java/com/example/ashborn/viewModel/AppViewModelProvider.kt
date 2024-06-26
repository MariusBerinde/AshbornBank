package com.example.ashborn.viewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ashborn.AshBornApp


class AppViewModelProvider {
    val Factory = viewModelFactory {
        //init viewModel
     /*   initializer {
            AshbornViewModel(
               this.ashbornApp(),
                context = LocalContext.current
            )

        }


      */



    }
}

fun CreationExtras.ashbornApp():AshBornApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AshBornApp)