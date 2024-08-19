package com.example.ashborn.view.login

import android.app.Application
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.viewModel.WelcomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Welcome(
    viewModel: WelcomeViewModel,
    navController: NavHostController,
) {
  //  viewModel.leggiDatiUtente()
    val connectionStatus by viewModel.networkConnectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Unavailable)

    ErroreConnessione(connectionStatus = connectionStatus) {
        //BloccoPIN(viewModel = viewModel) {
            Column(
                modifier = Modifier
                    .padding(MediumPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {


                Text(
                    //text = stringResource(id = R.string.app_name)+"\n"+stringResource(id = R.string.welcome)+"\n"+viewModel.userName,
                   // text = stringResource(id = R.string.app_name) + "\n" + stringResource(id = R.string.welcome) + "\n" + viewModel.dataStoreManager.usernameFlow.collectAsState(
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
}


/*
@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    // Creiamo una versione mock del ViewModel per la preview
    val mockApplication = Application() // Se hai bisogno di un contesto puoi usarlo qui
    val mockViewModel = WelcomeViewModel(mockApplication)

    // Richiamiamo il composable con il viewModel mock
    Welcome(viewModel = mockViewModel)
}*/
