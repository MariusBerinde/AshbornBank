package com.example.ashborn.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Utente(viewModel: AshbornViewModel, navController: NavHostController) {

    Text(text = "Benvenuto " + viewModel.userName)
}