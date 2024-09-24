package com.example.ashborn

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
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
import com.example.ashborn.viewModel.AskPinViewModel
import com.example.ashborn.viewModel.AskPinViewModelFactory
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

const val SOGLIA = 300000 // 5 minuti

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
    // Inizializza il CoroutineScope per lanciare coroutine
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Aggiungi un observer per monitorare quando l'app ritorna in foreground
         var lastBackgroundTime: Long = 0
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                // Quando l'app ritorna in foreground, naviga alla schermata del PIN
                val currentDestination = navController.currentDestination?.route
                val currentPos = navController.currentBackStackEntry?.destination?.route
               Log.d(nameFun,"posizione dove mi sveglio${currentPos}")
                val actualTime = System.currentTimeMillis()
                val tempoInBack = actualTime - lastBackgroundTime
                Log.d(nameFun,"tempoInBack = $tempoInBack")
                if(tempoInBack > SOGLIA) {
                    if (currentDestination != "welcome") {
                        scope.launch {
                            //    navController.navigate("login") // Naviga alla schermata del PIN
                            navController.navigate("login?prev=$currentPos")
                        }
                    }
                }


            }

            override fun onStop(owner: LifecycleOwner) {
                lastBackgroundTime = System.currentTimeMillis()
                // Quando l'app va in background, puoi aggiungere altra logica qui se necessario
            }
        })
    }

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
            composable("login?prev={prev}") {
                //    backStackEntry -> val prev = backStackEntry.arguments?.getString("prev").toBoolean()
                backStackEntry -> val prev = backStackEntry.arguments?.getString("prev")
                val viewModel: AskPinViewModel = viewModel(
                    factory = AskPinViewModelFactory(applicationContext as Application)
                )
                ErroreConnessione(connectionStatus = connectionStatus) {
                    AskPIN(
                        navController = navController,
                        viewModel = viewModel,
                        operation = null,
                        prevPos = prev
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
                composable("conti?index={index}") {

                        backStackEntry -> val index = backStackEntry.arguments?.getString("index")?.toInt()?:0
                    val contiViewmodel: ContiViewModel = viewModel(
                        factory = ContiViewModelFactory(applicationContext as Application)
                    )
                    val carteViewModel: CarteViewModel = viewModel(
                        factory = CarteViewModelFactory(applicationContext )
                    )
                    val altroViewModel: AltroViewModel = viewModel(
                        factory = AltroViewModelFactory(applicationContext )
                    )
                    val operationViewModel: OperationViewModel = viewModel(
                        factory = OperationViewModelFactory(applicationContext )
                    )
                    ErroreConnessione(connectionStatus = connectionStatus) {
                        Pagine(
                            navController = navController,
                            viewModelConti = contiViewmodel,
                            viewModelCarte = carteViewModel,
                            viewModelOperazioni = operationViewModel,
                            viewModelAltro = altroViewModel,
                            indice = index,
                        )
                    }
                }
                composable(route = "dettagli-operazione/{operazione}") {
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

            }
        navigation(
            startDestination = "bonifico",
            route = "operazioni"
        ){
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
                    json.decodeFromString(Operation.serializer(), data)
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

    }
}

