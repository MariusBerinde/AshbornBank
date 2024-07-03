package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.view.BloccoPIN
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Welcome(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
) {
    ErroreConnessione(connectionStatus = connectionStatus) {
        BloccoPIN(viewModel = viewModel) {
            Column(
                modifier = Modifier
                    .padding(MediumPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Log.i(
                    "Welcome",
                    viewModel.dataStoreManager.usernameFlow.collectAsState(initial = "val Iniziale").value
                )
                Text(
                    //text = stringResource(id = R.string.app_name)+"\n"+stringResource(id = R.string.welcome)+"\n"+viewModel.userName,
                    text = stringResource(id = R.string.app_name) + "\n" + stringResource(id = R.string.welcome) + "\n" + viewModel.dataStoreManager.usernameFlow.collectAsState(
                        initial = ""
                    ).value,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(SmallVerticalSpacing))
                Button(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(SmallPadding),
                    //TODO:aggiungere controllo per verifica primo login
                    onClick = {
                        if (viewModel.fistLogin) {
                            navController.navigate("primo-login")
                        } else {
                            navController.navigate("login")

                        }
                    }
                ) {
                    Text(stringResource(id = R.string.entra))
                }
            }
        }
    }
}

