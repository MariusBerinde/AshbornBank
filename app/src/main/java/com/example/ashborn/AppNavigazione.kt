package com.example.ashborn

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ashborn.view.Archivio
import com.example.ashborn.view.Avvisi
import com.example.ashborn.view.Impostazioni
import com.example.ashborn.view.Logout
import com.example.ashborn.view.Pagine
import com.example.ashborn.view.Sicurezza
import com.example.ashborn.view.Utente
import com.example.ashborn.view.login.AskPIN
import com.example.ashborn.view.login.Registrazione
import com.example.ashborn.view.login.Welcome
import com.example.ashborn.view.operazioni.Bonifico
import com.example.ashborn.view.operazioni.DettagliOperazione
import com.example.ashborn.view.operazioni.Mav
import com.example.ashborn.view.operazioni.OperazioneConfermata
import com.example.ashborn.view.operazioni.PINOperazione
import com.example.ashborn.view.operazioni.RiepilogoBonifico
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun AppNavigazione(
    viewModel: AshbornViewModel,
    operationViewModel: OperationViewModel,
    connectionStatus: ConnectivityObserver.Status
){
    val navController = rememberNavController()
    val startDest = viewModel.startDest
    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        Log.i("NavHost", connectionStatus.toString())
        navigation(
            startDestination = "welcome",
            route = "init"
        ) {
            composable("welcome") {
                Welcome(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
            composable("login") {
                AskPIN(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
            composable("primo-login") {
                Registrazione(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
        }
        navigation(
            startDestination = "conti",
            route = "principale"
        ) {
            composable("utente") {

               // val viewModel: PreviewAshbornViewModel = PreviewAshbornViewModel(PrevievApp())
                Utente(
                    navController = navController,
                    viewModel = viewModel,
                    viewModelp = null
                )
            }
            composable("conti") {
                Pagine(
                    navController = navController,
                    viewModel = viewModel,
                    connectionStatus = connectionStatus
                )
            }
            composable(
                route = "dettagli-operazione/{index_op}",
                arguments = listOf(navArgument("index_op") { type = NavType.LongType })
            ) { argomenti ->
                Log.d("AppNavigazione", "index " + argomenti.arguments!!.getLong("index_op").toString())
                DettagliOperazione(
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
                    viewModel = viewModel
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
               /*3 Mav(
                    navController = navController,
                    viewModel = viewModel
                )*/
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
            route = "operazioni"
        ) {
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