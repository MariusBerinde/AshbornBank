package com.example.ashborn.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.AppNavigazione
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Utente(viewModel: AshbornViewModel, navController: NavHostController) {

    Text(text = "Benvenuto " + viewModel.userName)
}


@Preview(showBackground = true)
@Composable
fun AshbornPreview() {
    AshbornTheme {
        val viewModel: AshbornViewModel = AshbornViewModel()
        val navController = rememberNavController()
        Utente(viewModel = viewModel, navController = navController)
    }
}