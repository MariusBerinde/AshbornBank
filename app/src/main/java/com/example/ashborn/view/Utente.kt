package com.example.ashborn.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.viewModel.AppViewModelProvider
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Utente(
    viewModel: AshbornViewModel,
    navController: NavHostController
) {
    Text(text = stringResource(id = R.string.welcome) +" "+ viewModel.userName)
}


@Preview(showBackground = true)
@Composable
fun AshbornPreview() {
    AshbornTheme {

        val viewModel: AshbornViewModel = viewModel(factory = AppViewModelProvider().Factory)
        val navController = rememberNavController()
        Utente(viewModel = viewModel, navController = navController)
    }
}

