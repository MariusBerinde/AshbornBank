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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.data.User
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.view.BloccoPIN
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.WelcomeViewModel

@Composable
fun Welcome(
    viewModel: WelcomeViewModel ,
) {
  //  viewModel.leggiDatiUtente()
//    ErroreConnessione(connectionStatus = connectionStatus) {
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
                      /*  if (
                            viewModel.userName != ""
                        ){
                            Log.d("Welcome","verso conti")
                        }
                        else{

                            Log.d("Welcome","verso registrazione")
                        }*/
                       /*
                        if (viewModel.fistLogin) {
                            navController.navigate("primo-login")
                        } else {
                            navController.navigate("login")

                        }*/
                    }
                ) {
                    Text(stringResource(id = R.string.entra))
                }
            }
}
/*
@Preview(showBackground = true)
@Composable
fun PrevWelcome(){
   // val viewModel = WelcomeViewModel(user = User("test","","","",""))

    AshbornTheme {
      Welcome(null )
    }
}*/