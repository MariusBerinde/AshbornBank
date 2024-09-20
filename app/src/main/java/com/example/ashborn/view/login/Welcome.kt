package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
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
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(SmallVerticalSpacing))
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(SmallPadding),
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