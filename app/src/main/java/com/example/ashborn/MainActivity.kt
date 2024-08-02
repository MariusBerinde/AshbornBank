package com.example.ashborn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ashborn.data.User
import com.example.ashborn.model.DataStoreManager
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.view.login.Welcome
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel
import com.example.ashborn.viewModel.WelcomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContent {
            AshbornTheme {
                val dataStoreManager = DataStoreManager(application)
           /*     runBlocking (Dispatchers.IO){

                    dataStoreManager.writeUserPrefernces(User("Tom","Riddle","","",""))
                }*/
               val viewModel = WelcomeViewModel(dataStoreManager)
                //val viewModel2 = WelcomeViewModel(user=null)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                //    Welcome(viewModel = viewModel)
                  AppNavigazione2(startDest = "init", viewModel =viewModel )

                }
            }
        }
    }


}
/*class MainActivity : ComponentActivity() {

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


}*/
