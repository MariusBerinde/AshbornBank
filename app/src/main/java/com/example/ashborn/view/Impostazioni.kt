package com.example.ashborn.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ashborn.R

@Composable
fun Impostazioni(
    navController: NavHostController,
    //viewModel: AshbornViewModel
) {
    Text(text = stringResource(id = R.string.impostazioni))
}