package com.example.ashborn

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ashborn.data.Operation
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.view.Archivio
import com.example.ashborn.view.Avvisi
import com.example.ashborn.view.Impostazioni
import com.example.ashborn.view.Logout
import com.example.ashborn.view.Pagine
import com.example.ashborn.view.Sicurezza
import com.example.ashborn.view.login.AskPIN
import com.example.ashborn.view.login.Registrazione
import com.example.ashborn.view.login.Welcome
import com.example.ashborn.view.operazioni.DettagliOperazione
import com.example.ashborn.view.operazioni.OperazioneConfermata
import com.example.ashborn.view.operazioni.RiepilogoBonifico
import com.example.ashborn.viewModel.AltroViewModel
import com.example.ashborn.viewModel.AltroViewModelFactory
import com.example.ashborn.viewModel.CarteViewModel
import com.example.ashborn.viewModel.CarteViewModelFactory
import com.example.ashborn.viewModel.ContiViewModel
import com.example.ashborn.viewModel.ContiViewModelFactory
import com.example.ashborn.viewModel.DettagliOperazioneViewModel
import com.example.ashborn.viewModel.DettagliOperazioneViewModelFactory
import com.example.ashborn.viewModel.OperationViewModel
import com.example.ashborn.viewModel.OperationViewModelFactory
import com.example.ashborn.viewModel.WelcomeViewModel
import com.example.ashborn.viewModel.WelcomeViewModelFactory
import kotlinx.serialization.json.Json

@Composable
fun OperazioneRifiutata(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}

@Composable
fun AppNavigazione2(
    startDest:String,
    //connectionStatus: ConnectivityObserver.Status
){
    val nameFun = object {}.javaClass.enclosingMethod?.name

    val navController = rememberNavController()
    //val startDest = startDest
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
            composable("login") {
                val viewModel: AskPinViewModel = viewModel(
                    factory = AskPinViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                AskPIN(
                    navController = navController,
                    viewModel = viewModel,
                    operation = null
                    //connectionStatus = connectionStatus
                )
            }
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
            navigation(
                startDestination = "riepilogo-bonifico",
                route = "operazioni",
            ) {
                composable("pin/{operazione}") {
                    val viewModel: AskPinViewModel = viewModel(
                        factory = AskPinViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = Json{ prettyPrint = true }.decodeFromString(Operation.serializer(), jsonData)
                    AskPIN(
                        navController = navController,
                        viewModel = viewModel,
                        operation = operation,
                    )
                }
                composable("operazioneConfermata") {
                    OperazioneConfermata(
                        navController = navController,
                    )
                }
                composable("operazioneRifiutata"){
                    OperazioneRifiutata(
                        navController = navController
                    )
                }
            }
            navigation(
                startDestination = "conti",
                route = "principale"
            ) {
                /*composable("utente") {

                    // val viewModel: PreviewAshbornViewModel = PreviewAshbornViewModel(PrevievApp())
                    Utente(
                        navController = navController,
                        viewModel = viewModel,
                        viewModelp = null
                    )
                }*/
                composable("conti") {
                    val contiViewmodel: ContiViewModel = viewModel(
                        factory = ContiViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    val carteViewModel: CarteViewModel = viewModel(
                        factory = CarteViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    val altroViewModel: AltroViewModel = viewModel(
                        factory = AltroViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    val operationViewModel: OperationViewModel = viewModel(
                        factory = OperationViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    Pagine(
                        navController = navController,
                        viewModelConti = contiViewmodel,
                        viewModelCarte = carteViewModel,
                        viewModelOperazioni = operationViewModel,
                        viewModelAltro = altroViewModel,
                        //connectionStatus = connectionStatus
                    )
                }
                composable(
                    route = "dettagli-operazione/{operazione}",
                    ) {
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = Json{ prettyPrint = true }.decodeFromString(Operation.serializer(), jsonData)
                    val dettagliOperazioneViewModel : DettagliOperazioneViewModel = viewModel(
                        factory = DettagliOperazioneViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    DettagliOperazione(
                        operation = operation,
                        navController = navController, viewModel = dettagliOperazioneViewModel
                    )
                }
                composable("avvisi") {
                    Avvisi(
                        navController = navController,
                        //viewModel = viewModel
                    )
                }
                composable("archivio") {
                    Archivio(
                        navController = navController,
                        //viewModel = viewModel
                    )
                }
                composable("sicurezza") {
                    Sicurezza(
                        navController = navController,
                        //viewModel = viewModel
                    )
                }
                composable("impostazioni") {
                    Impostazioni(
                        navController = navController,
                        //viewModel = viewModel
                    )
                }
                composable("logout") {
                    Logout(
                        navController = navController,
                        //viewModel = viewModel
                    )
                }
                /*
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
                }*/
                composable("riepilogo-bonifico/{operazione}") {
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = Json{ prettyPrint = true }.decodeFromString(Operation.serializer(), jsonData)
                    RiepilogoBonifico(
                        navController = navController,
                        operation = operation,
                    )
                }
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