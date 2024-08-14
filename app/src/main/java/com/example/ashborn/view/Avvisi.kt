package com.example.ashborn.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.viewModel.AvvisiViewModel

@Composable
fun Avvisi(
    navController: NavHostController,
    viewModel: AvvisiViewModel,
) {
    Text(text = stringResource(id = R.string.avvisi))
}