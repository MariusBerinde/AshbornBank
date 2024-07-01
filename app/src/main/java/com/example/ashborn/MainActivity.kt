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
import com.example.ashborn.ui.theme.AshbornTheme
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
/*
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
*/

