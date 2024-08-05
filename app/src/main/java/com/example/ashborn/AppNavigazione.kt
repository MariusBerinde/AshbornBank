package com.example.ashborn

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.view.login.AskPIN
import com.example.ashborn.view.login.Registrazione
import com.example.ashborn.view.login.Welcome
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.WelcomeViewModel
import com.example.ashborn.viewModel.WelcomeViewModelFactory

@Composable
fun AppNavigazione2(
    startDest:String,
    //connectionStatus: ConnectivityObserver.Status
){

    val navController = rememberNavController()
    val startDest = startDest

    NavHost(
        navController = navController,
        startDestination = startDest
    ){
        navigation(
            startDestination = "welcome",
            route = "init"
        ) {
            composable("welcome") {
                val viewModel: WelcomeViewModel = viewModel(
                    factory = WelcomeViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                //  Welcome(viewModel = viewModel)
                Welcome(navController = navController, viewModel = viewModel)
            }
            /*composable("login") {
                AskPIN(
                    navController = navController,
                    viewModel = AskPinViewModel(),
                    //connectionStatus = connectionStatus
                )
            }*/
            composable("primo-login") {
                val viewModel: RegistrazioneViewModel = viewModel(
                    factory = RegistrazioneViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                Registrazione(
                    navController = navController,
                    viewModel = viewModel,
                    //connectionStatus = connectionStatus
                )
            }
    }
}
}

    /*
@Composable
fun AppNavigazione(
    viewModel: AshbornViewModel,
    viewModel2: WelcomeViewModel,
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
                    //navController = navController,
                    viewModel = viewModel2,
                   // connectionStatus = connectionStatus
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
                Mav(
                    navController = navController,
                    viewMorunBlocking (Dispatchers.IO){

                    dataStoreManager.writeUserPrefernces(User("Tom","Riddle","","",""))
                }del = operationViewModel
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
}
     */