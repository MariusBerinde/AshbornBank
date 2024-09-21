package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.AskPinViewModel
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.NavigationEvent
import com.example.ashborn.NetworkConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.OperationType
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing


@Composable
fun AskPIN(
    navController: NavHostController,
    viewModel: AskPinViewModel,
    operation: Operation?,
    prev:Boolean = false,
    prevPos:String? = null,
) {
    Log.i("AskPIN", "renderizzo AskPIN")
    val networkConnectivityObserver = NetworkConnectivityObserver.getInstance(LocalContext.current.applicationContext)
    val connectionStatus by networkConnectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Unavailable)
    val navigationState by viewModel.navigationState.observeAsState()

    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(id = R.string.pin))
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = viewModel.pin,
                onValueChange = { viewModel.setPinX(it) },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Spacer(modifier = Modifier.height(MediumPadding))

        // Creazione dei pulsanti per i numeri
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                    Button(
                        modifier = Modifier.size(70.dp, 40.dp),
                        onClick = {
                            if (viewModel.pin.length <= 8) {
                                viewModel.setPinX(viewModel.pin + (3 * i + j + 1).toString())
                            }
                        }) {
                        Text(text = (3 * i + j + 1).toString())
                    }
                }
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
        }

        Row {
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    viewModel.validatePin()
                }
            ) {
                Text(text = "OK")
            }

            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    if (viewModel.pin.length <= 8) {
                        viewModel.setPinX(viewModel.pin + "0")
                    }
                }) {
                Text(text = "0")
            }

            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    if (viewModel.pin.isNotEmpty()) {
                        viewModel.setPinX(viewModel.pin.dropLast(1))
                    }
                }
            ) {
                Icon(Icons.Filled.Clear, contentDescription = "icona cancellazione")
            }

            // Osserva il cambio di stato di navigazione
            LaunchedEffect(navigationState) {
                Log.i("AskPIN", "valore navigazione= ${navigationState.toString()}")
                when (navigationState) {
                    NavigationEvent.NavigateToNext -> {
                        viewModel.resetWrongAttempts()
                       /* if (prev ){
                            Log.d("AskPin","sto lanciando onPinEntered")
                           // onPinEntered() // Chiama il callback qui per indicare che il PIN è stato inserito correttamente
                            navController.popBackStack()
                        }*/
                        if(prevPos != null){
                            Log.d("AskPin","navigo verso $prevPos")

                            navController.navigate(prevPos.toString())
                        }



                        if (operation == null) {
                            navController.navigate("conti")
                        } else {
                            when (operation.operationStatus) {
                                OperationStatus.TO_DELETE -> {
                                    viewModel.revocaOperazione(operation)
                                    navController.navigate("operazioneConfermata")
                                }
                                else -> {
                                    when (operation.operationType) {
                                        OperationType.WIRE_TRANSFER -> {
                                            viewModel.executeTransaction(operation)
                                            navController.navigate("operazioneConfermata")
                                        }
                                        else -> {
                                            viewModel.executeInstantTransaction(operation)
                                            navController.navigate("operazioneConfermata")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    NavigationEvent.NavigateToError, NavigationEvent.NavigateToErrorAlt -> {
                        Log.i("AskPIN", "errore e numero di tentativi ${viewModel.wrongAttempts}")
                        if (viewModel.wrongAttempts > 3) {
                            if (operation == null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("operazioneRifiutata")
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}



