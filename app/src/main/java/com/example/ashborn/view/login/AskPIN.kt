package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.viewModel.AskPinViewModel
import com.example.ashborn.viewModel.NavigationEvent
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.OperationType
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun AskPIN(
    navController: NavHostController,
    viewModel: AskPinViewModel,
    operation: Operation?,
) {
    val nameFun = object{}.javaClass.enclosingMethod?.name
    Log.d(nameFun, "renderizzo AskPIN")
    val navigationState by viewModel.navigationState.observeAsState()
    val isBlocked = viewModel.isBlocked

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(MediumPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(start = LargePadding, end = LargePadding),
            ) {
                Text(
                    text = stringResource(id = R.string.pin),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth().padding(start = LargePadding, end = LargePadding, )
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.pin,
                    onValueChange = { viewModel.setPinX(it) },
                    readOnly = true,
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    textStyle = TextStyle.Default.copy(fontSize = 35.sp, textAlign = TextAlign.Center),
                )
            }
            Spacer(modifier = Modifier.height(MediumPadding))

            for (i in (0..2)) {
                Row {
                    for (j in (0..2)) {

                        Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                        Button(
                            modifier = Modifier.size(100.dp, 50.dp),
                            onClick = {
                                if (viewModel.pin.length <= 8) {
                                    viewModel.setPinX(viewModel.pin + (3 * i + j + 1).toString())
                                }
                            }) {
                            Text(text = (3 * i + j + 1).toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(SmallPadding))

            }
            Row {
                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                    modifier = Modifier.size(100.dp, 50.dp),
                    onClick = {

                        viewModel.validatePin()
                    }
                ) {
                    Text(text = "OK", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                    modifier = Modifier.size(100.dp, 50.dp),
                    onClick = {
                        if (viewModel.pin.length <= 8) {
                            viewModel.setPinX(viewModel.pin + "0")
                        }
                    }) {
                    Text(text = "0", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                    modifier = Modifier.size(100.dp, 50.dp),
                    onClick = {
                        if (viewModel.pin.isNotEmpty()) {
                            viewModel.setPinX(viewModel.pin.dropLast(1))
                        }
                    }
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = "icona cancellazione")
                }
                //FIXME: capire perché non mostra l'overlay

                LaunchedEffect(navigationState) {
                    Log.i(nameFun,"valore navigazione= ${navigationState.toString()}")
                    when(navigationState){
                        NavigationEvent.NavigateToNext -> {
                            viewModel.resetWrongAttempts()
                            if (operation == null) {
                                navController.navigate("conti")
                            } else {
                                if ( operation.operationStatus == OperationStatus.TO_DELETE )
                                   viewModel.revocaOperazione(operation)
                                else if(operation.operationType == OperationType.WIRE_TRANSFER)
                                    viewModel.executeTransaction(operation)
                                else
                                    viewModel.executeInstantTransaction(operation)
                                navController.navigate("operazioneConfermata")
                            }

                        }
                        NavigationEvent.NavigateToError, NavigationEvent.NavigateToErrorAlt -> {
                            Log.i(nameFun, "errore e numero di tentativi ${viewModel.wrongAttempts}")
                            viewModel.writeWrongAttempts()
                            if(viewModel.remainingTime > 0 && !viewModel.timerIsRunning)
                                viewModel.startTimer()
                            if(viewModel.wrongAttempts > 3 ) {
                                if (operation == null) {
                                    //navController.popBackStack()
                                    viewModel.startTimer()
                                    Log.d(nameFun, "start timer askpin")

                                } else {
                                    navController.navigate("operazioneRifiutata")
                                }

                            }
                        }
                        else->{}
                    }

                }
            }
        }
        Log.d(nameFun, "isBlocked: $isBlocked")
        if (isBlocked) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                color = Color.Transparent

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(LargePadding)
                        .fillMaxSize(),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.timer) + "\n" + viewModel.formatRemainingTime(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(LargePadding)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}