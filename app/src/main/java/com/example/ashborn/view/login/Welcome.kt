package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.WelcomeViewModel

@Composable
fun Welcome(
    viewModel: WelcomeViewModel,
    navController: NavHostController,
) {
     //BloccoPIN(viewModel = viewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
/*
        var timerIsRunning = viewModel.timerIsRunning
*/      Column(
            modifier = Modifier
                .padding(MediumPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.app_name) + "\n" + stringResource(id = R.string.welcome) + "\n" + viewModel.getUsername(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(MediumPadding),
                //TODO:aggiungere controllo per verifica primo login
                onClick = {
                    if (viewModel.getUsername().isBlank()) {
                        Log.d("Welcome","verso registrazione")
                        navController.navigate("primo-login")
                    } else {
                        Log.d("Welcome","verso pin")
                        navController.navigate("login")
                        Log.d("welcome", viewModel.getUsername())
                    }
                }
            ) {
                Text(stringResource(id = R.string.entra))
            }
        }
    }
    //}
}