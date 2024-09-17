package com.example.ashborn

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.Operation
import com.example.ashborn.view.AnnullaOperazione
import com.example.ashborn.view.Avvisi
import com.example.ashborn.view.DettagliAvviso
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.view.Impostazioni
import com.example.ashborn.view.IstruzioniDisconiscimento
import com.example.ashborn.view.Pagine
import com.example.ashborn.view.Sicurezza
import com.example.ashborn.view.Utente
import com.example.ashborn.view.login.AskPIN
import com.example.ashborn.view.login.Registrazione
import com.example.ashborn.view.login.Welcome
import com.example.ashborn.view.operazioni.Archivio
import com.example.ashborn.view.operazioni.Bonifico
import com.example.ashborn.view.operazioni.DettagliOperazione
import com.example.ashborn.view.operazioni.Mav
import com.example.ashborn.view.operazioni.MavManuale
import com.example.ashborn.view.operazioni.MavQrCode
import com.example.ashborn.view.operazioni.OperazioneConfermata
import com.example.ashborn.view.operazioni.OperazioneRifiutata
import com.example.ashborn.view.operazioni.RiepilogoOperazione
import com.example.ashborn.viewModel.AltroViewModel
import com.example.ashborn.viewModel.AltroViewModelFactory
import com.example.ashborn.viewModel.AvvisiViewModel
import com.example.ashborn.viewModel.AvvisiViewModelFactory
import com.example.ashborn.viewModel.BonificoViewModel
import com.example.ashborn.viewModel.BonificoViewModelFactory
import com.example.ashborn.viewModel.CarteViewModel
import com.example.ashborn.viewModel.CarteViewModelFactory
import com.example.ashborn.viewModel.ContiViewModel
import com.example.ashborn.viewModel.ContiViewModelFactory
import com.example.ashborn.viewModel.DettagliOperazioneViewModel
import com.example.ashborn.viewModel.DettagliOperazioneViewModelFactory
import com.example.ashborn.viewModel.MavViewModel
import com.example.ashborn.viewModel.MavViewModelFactory
import com.example.ashborn.viewModel.OperationViewModel
import com.example.ashborn.viewModel.OperationViewModelFactory
import com.example.ashborn.viewModel.UtenteViewModel
import com.example.ashborn.viewModel.UtenteViewModelFactory
import com.example.ashborn.viewModel.WelcomeViewModel
import com.example.ashborn.viewModel.WelcomeViewModelFactory
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun AppNavigazione2(
    startDest:String,
){
    val nameFun = object {}.javaClass.enclosingMethod?.name
    val applicationContext = LocalContext.current.applicationContext
    val navController = rememberNavController()
    val networkConnectivityObserver = NetworkConnectivityObserver.getInstance(applicationContext)
    val connectionStatus by networkConnectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Unavailable)
    val json = Json { prettyPrint = true }

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
                    factory = WelcomeViewModelFactory(applicationContext as Application)
                )
                ErroreConnessione(connectionStatus = connectionStatus) {
                    Welcome(navController = navController, viewModel = viewModel)
                }
            }
            composable("login") {
                val viewModel: AskPinViewModel = viewModel(
                    factory = AskPinViewModelFactory(applicationContext as Application)
                )
                ErroreConnessione(connectionStatus = connectionStatus) {
                    AskPIN(
                        navController = navController,
                        viewModel = viewModel,
                        operation = null,
                    )
                }
            }
            composable("primo-login") {
                val viewModel: RegistrazioneViewModel = viewModel(
                    factory = RegistrazioneViewModelFactory(applicationContext as Application)
                )
                ErroreConnessione(connectionStatus = connectionStatus) {
                    Registrazione(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
            navigation(
                startDestination = "riepilogo-operazione",
                route = "operazioni",
            ) {
                composable("pin/{operazione}") {
                    val viewModel: AskPinViewModel = viewModel(
                        factory = AskPinViewModelFactory(applicationContext as Application)
                    )
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = json.decodeFromString(Operation.serializer(), jsonData)
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        AskPIN(
                            navController = navController,
                            viewModel = viewModel,
                            operation = operation,
                        )
                    }
                }
                composable("operazioneConfermata") {
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        OperazioneConfermata(
                            navController = navController,
                        )
                    }
                }
                composable("operazioneRifiutata"){
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        OperazioneRifiutata(navController = navController)
                    }
                }
            }
            navigation(
                startDestination = "conti",
                route = "principale"
            ) {
                composable("utente") {
                    val utenteViewModel: UtenteViewModel = viewModel(
                        factory = UtenteViewModelFactory(applicationContext as Application)
                    )
                    // val viewModel: PreviewAshbornViewModel = PreviewAshbornViewModel(PrevievApp())
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Utente(
                            navController = navController,
                            viewModel = utenteViewModel
                        )
                    }
                }
                composable("conti") {
                    val contiViewmodel: ContiViewModel = viewModel(
                        factory = ContiViewModelFactory(applicationContext as Application)
                    )
                    val carteViewModel: CarteViewModel = viewModel(
                        factory = CarteViewModelFactory(applicationContext as Application)
                    )
                    val altroViewModel: AltroViewModel = viewModel(
                        factory = AltroViewModelFactory(applicationContext as Application)
                    )
                    val operationViewModel: OperationViewModel = viewModel(
                        factory = OperationViewModelFactory(applicationContext as Application)
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Pagine(
                            navController = navController,
                            viewModelConti = contiViewmodel,
                            viewModelCarte = carteViewModel,
                            viewModelOperazioni = operationViewModel,
                            viewModelAltro = altroViewModel,
                        )
                    }
                }
                composable(
                    route = "dettagli-operazione/{operazione}",
                ) {
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = json.decodeFromString(Operation.serializer(), jsonData)
                    val dettagliOperazioneViewModel : DettagliOperazioneViewModel = viewModel(
                        factory = DettagliOperazioneViewModelFactory(applicationContext as Application)
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        DettagliOperazione(
                            operation = operation,
                            navController = navController, viewModel = dettagliOperazioneViewModel
                        )
                    }
                }
                composable("avvisi") {
                    val avvisiOperazioneViewModel : AvvisiViewModel = viewModel(
                        factory = AvvisiViewModelFactory(applicationContext as Application)
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Avvisi(
                            navController = navController,
                            viewModel = avvisiOperazioneViewModel
                        )
                    }
                }
                composable("dettagli-avviso/{avviso}") {
                    val jsonData = it.arguments?.getString("avviso") ?: "No Data"
                    val avviso = json.decodeFromString(Avviso.serializer(), jsonData)
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        DettagliAvviso(
                            navController =  navController,
                            avviso = avviso
                        )
                    }
                }
                composable("archivio") {
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Archivio(
                            navController = navController,
                            //viewModel = viewModel
                        )
                    }
                }
                composable("sicurezza") {
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Sicurezza(
                            navController = navController,
                            //viewModel = viewModel
                        )
                    }
                }
                composable("impostazioni") {
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Impostazioni(
                            navController = navController,
                            //viewModel = viewModel
                        )
                    }
                }
                composable("bonifico") {
                    val bonificoViewModel : BonificoViewModel = viewModel(
                        factory = BonificoViewModelFactory(applicationContext as Application)
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Bonifico(
                            navController = navController,
                            viewModel = bonificoViewModel,
                        )
                    }
                }
                composable("mav") {
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Mav(navController = navController)
                    }
                }
                composable("scan-qrcode") {
                    val mavViewModel : MavViewModel = viewModel(
                        factory = MavViewModelFactory(applicationContext as Application)
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        MavQrCode(
                            navController = navController,
                            viewModel = mavViewModel
                        )
                    }
                }
                composable("mav-manuale?operazione={operazione}") {
                    val mavViewModel : MavViewModel = viewModel(
                        factory = MavViewModelFactory(applicationContext as Application)
                    )
                    //val jsonData = it.arguments?.getString("operazione") ?: "No Data

                    val jsonData = it.arguments?.getString("operazione")
                    val operation: Operation? = jsonData?.let { data ->
                        // Decodifica JSON in un oggetto Operation
                        Json { prettyPrint = true }.decodeFromString(Operation.serializer(), data)
                    }
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        MavManuale(
                            navController = navController,
                            viewModel = mavViewModel,
                            operation = operation,
                        )
                    }
                }
                composable("riepilogo-operazione/{operazione}") {
                    val jsonData = it.arguments?.getString("operazione") ?: "No Data"
                    val operation: Operation = json.decodeFromString(Operation.serializer(), jsonData)
                    RiepilogoOperazione(
                        navController = navController,
                        operation = operation,
                    )
                }
                composable("disconosci-operazione"){

                    IstruzioniDisconiscimento(
                        navController = navController,
                        )
                }
                composable("annulla-operazione/{operation}"){
                    val jsonData = it.arguments?.getString("operation") ?: "No Data"
                    val operation: Operation = json.decodeFromString(Operation.serializer(), jsonData)
                    AnnullaOperazione(
                        navController = navController,
                        operazion = operation
                    )
                }
            }
        }
    }
}

