package com.example.ashborn

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.view.Archivio
import com.example.ashborn.view.AskPIN
import com.example.ashborn.view.Avvisi
import com.example.ashborn.view.Bonifico
import com.example.ashborn.view.DettagliOperazione
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.view.Impostazioni
import com.example.ashborn.view.Logout
import com.example.ashborn.view.Mav
import com.example.ashborn.view.OperazioneConfermata
import com.example.ashborn.view.PINOperazione
import com.example.ashborn.view.Pagine
import com.example.ashborn.view.Registrazione
import com.example.ashborn.view.RiepilogoBonifico
import com.example.ashborn.view.Sicurezza
import com.example.ashborn.view.Utente
import com.example.ashborn.view.Welcome
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel

class MainActivity : ComponentActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            AshbornTheme {
                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Unavailable
                )
                val viewModel: AshbornViewModel by viewModels()
                val operationViewModel: OperationViewModel by viewModels()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigazione(
                        viewModel = viewModel,
                        operationViewModel = operationViewModel,
                        connectionStatus = status
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigazione(
    viewModel: AshbornViewModel,
    operationViewModel: OperationViewModel,
    connectionStatus: ConnectivityObserver.Status
){
    val navController = rememberNavController()
    val startDest = viewModel.startDest;
    NavHost(
        navController = navController,
        startDestination = startDest
    ){
        Log.i("NavHost", connectionStatus.toString())
        navigation(
            startDestination = "welcome",
            route="init"
        ){
            composable("welcome"){
                Welcome(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
            composable("login"){
                AskPIN(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus= connectionStatus
                )
            }
            composable("primo-login"){
                Registrazione(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus= connectionStatus
                )
            }
        }
        navigation(
            startDestination = "conti",
            route="principale"
        ){
            composable("utente"){
                Utente(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("conti"){
                Pagine(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
            composable(
                route = "dettagli-operazione/{index_op}",
                arguments = listOf(navArgument("index_op") { type = NavType.LongType })
            ){
                argomenti-> DettagliOperazione(
                    indexOperation = argomenti.arguments!!.getLong("index_op"),
                    navController = navController, viewModel = viewModel
                )
            }
            composable("avvisi") {
                Avvisi(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("archivio") {
                Archivio(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("sicurezza") {
                Sicurezza(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("impostazioni") {
                Impostazioni(
                    navController = navController,
                    viewModel =viewModel
                )
            }
            composable("logout") {
                Logout(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("bonifico") {
                Bonifico(
                    navController = navController,
                    viewModelOp = operationViewModel
                )
            }
            composable("mav") {
                Mav(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable("riepilogo-bonifico") {
                RiepilogoBonifico(
                    navController = navController,
                    viewModel = operationViewModel
                )
            }
        }
        navigation(
            startDestination = "riepilogo-bonifico",
            route="operazioni"
        ){
            composable("pin") {
                PINOperazione(
                    navController = navController,
                    viewModel = operationViewModel
                )
            }
            composable("operazioneConfermata") {
                OperazioneConfermata(
                    navController = navController,
                    viewModel = operationViewModel
                )
            }
           /* composable("operazioneRifiutata"){

            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AshbornPreview() {
    AshbornTheme {
        val viewModel: AshbornViewModel = AshbornViewModel()
        val operationViewModel: OperationViewModel = OperationViewModel()
        AppNavigazione(
            viewModel = viewModel,
            operationViewModel = operationViewModel,
            connectionStatus = ConnectivityObserver.Status.Available
        )
    }
}
