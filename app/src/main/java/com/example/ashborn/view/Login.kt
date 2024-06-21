@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.data.User
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Welcome(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
) {
    ErroreConnessione(connectionStatus = connectionStatus) {
        Column (
            modifier = Modifier
                .padding(MediumPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.app_name)+"\n"+stringResource(id = R.string.welcome)+"\n"+viewModel.userName,
                 modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(SmallVerticalSpacing))
            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(SmallPadding),
                //TODO:aggiungere controllo per verifica primo login
                onClick = {
                    if (viewModel.fistLogin) {
                        navController.navigate("primo-login")
                    } else {
                        navController.navigate("login")
                    }
                }
            ) {
                Text(stringResource(id = R.string.entra))
            }
        }
    }
}

@Composable
fun AskPIN(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
) {
    ErroreConnessione(connectionStatus = connectionStatus) {
        Column (
            modifier = Modifier
                .padding(MediumPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(stringResource(id = R.string.pin))
            }
            Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
                OutlinedTextField(
                    value = viewModel.pin,
                    onValueChange = { viewModel.setPinX(it) },
                    readOnly = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            Spacer(modifier = Modifier.height(MediumPadding))
            //Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {


            for (i in (0 .. 2)) {
                Row {
                    for (j in (0 .. 2)) {

                        Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                        Button(
                            modifier = Modifier.size(70.dp,40.dp),
                            onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + (3*i+j).toString())}}) {
                            Text(text = (3*i+j+1).toString())
                        }
                    }
                }


                Spacer(modifier = Modifier.padding(SmallPadding))

            }
            Row {
                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                    modifier = Modifier.size(70.dp,40.dp),
                    onClick = {
                        Log.i("AskPIN", (!viewModel.checkPin()).toString())
                        if(viewModel.checkPin()) {
                            viewModel.setStartdest("principale")
                            navController.navigate("conti") //{popUpTo("principale")}
                            //navController.navigate("conti")
                        } else {
                            viewModel.incrementWrongAttempts()
                            Log.i("AskPIN", viewModel.wrongAttempts.toString())
                        }
                    }
                ) {
                    Text(text = "OK")
                }

                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                    modifier = Modifier.size(70.dp,40.dp),
                    onClick = { if(viewModel.pin.length <= 8) { viewModel.setPinX(viewModel.pin + "0") } }) {
                    Text(text = "0")
                }

                Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                Button(
                   modifier = Modifier.size(70.dp,40.dp),
                   onClick = {if(viewModel.pin.isNotEmpty()) {viewModel.setPinX(viewModel.pin.dropLast(1))} }) {
                   Icon( Icons.Filled.Clear, contentDescription = "icona cancellazione")
                }
            }
        }
    }
}
@Composable
fun Registrazione(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
) {
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    ErroreConnessione(connectionStatus = connectionStatus) {
        Column (
            modifier = Modifier
                .padding(MediumPadding)
                .padding(SmallPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 40.sp
            ) //TODO: da sistemare
            Spacer(modifier = Modifier.height(SmallPadding))
            OutlinedTextField(
                value = viewModel.userName,
                onValueChange = { viewModel.setUserNameX(it)  },
                label = { Text(stringResource(id = R.string.inserisci_nome) )},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if(viewModel.erroreNome == 1) {
                        Red
                    } else {
                        Black
                    },
                    unfocusedBorderColor = if(viewModel.erroreNome == 1) {
                        Red
                    } else {
                        Black
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester2.requestFocus() }
                ),
                modifier = Modifier
                    .focusRequester(focusRequester1)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(LargePadding))
            OutlinedTextField(
                value = viewModel.cognome,
                onValueChange = {viewModel.setCognomeX(it)},
                label = { Text( stringResource (R.string.inserisci_cognome)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if(viewModel.erroreCognome == 1) {
                        Red
                    } else {
                        Black
                    },
                    unfocusedBorderColor = if(viewModel.erroreCognome == 1) {
                        Red
                    } else {
                        Black
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester3.requestFocus() }
                ),
                modifier = Modifier
                    .focusRequester(focusRequester2)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LargePadding))
            OutlinedTextField(
                value = viewModel.dataNascita,
                onValueChange = {viewModel.setDataNascitaX(it)},
                label = { Text(stringResource(id = R.string.ins_data_nascita))},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if(viewModel.erroreDataNascita == 1) {
                        Red
                    } else {
                        Black
                    },
                    unfocusedBorderColor = if(viewModel.erroreDataNascita == 1) {
                        Red
                    } else {
                        Black
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester4.requestFocus() }
                ),
                modifier = Modifier
                    .focusRequester(focusRequester3)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(LargePadding))
            OutlinedTextField(
                value = viewModel.codCliente,
                onValueChange = {viewModel.setCodClienteX(it)},
                label = { Text(stringResource (R.string.inserisci_codice_cliente)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if(viewModel.erroreCodCliente == 1) {
                        Red
                    } else {
                        Black
                    },
                    unfocusedBorderColor = if(viewModel.erroreCodCliente == 1) {
                        Red
                    } else {
                        Black
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (
                            viewModel.formatoNomeValido(viewModel.userName) &&
                            viewModel.formatoDataNascitaValida(viewModel.dataNascita) &&
                            viewModel.formatoCognomeValido(viewModel.cognome) &&
                            viewModel.formatoCodiceCliente(viewModel.codCliente)
                        ) {
                            viewModel.fistLogin = false
                            navController.navigate("welcome")
                        } else {
                            gestisciErrori(
                                viewModel.userName,
                                viewModel.cognome,
                                viewModel.codCliente,
                                viewModel.dataNascita,
                                viewModel
                            )
                        }
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .focusRequester(focusRequester4)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(LargePadding))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (
                        viewModel.formatoNomeValido(viewModel.userName) &&
                        viewModel.formatoDataNascitaValida(viewModel.dataNascita) &&
                        viewModel.formatoCognomeValido(viewModel.cognome) &&
                        viewModel.formatoCodiceCliente(viewModel.codCliente)
                    ) {
                        viewModel.fistLogin=false
                        viewModel.upsertUser(
                            User(
                                viewModel.userName,
                                viewModel.dataNascita,
                                viewModel.cognome,
                               viewModel.codCliente
                            )
                        )
                        navController.navigate("welcome")
                    } else {
                        gestisciErrori(
                            viewModel.userName,
                            viewModel.cognome,
                            viewModel.codCliente,
                            viewModel.dataNascita,viewModel
                        )
                    }
                }) {
                Text(text = stringResource(id = R.string.conferma))
            }
        }
    }
}
@Composable
fun ErroreConnessione(
    connectionStatus: ConnectivityObserver.Status,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        content(this)
        if(connectionStatus == ConnectivityObserver.Status.Lost ||
            connectionStatus == ConnectivityObserver.Status.Unavailable) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Transparent)
                    .align(Alignment.Center),
                color = Transparent

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(LargePadding)
                ) {
                    Card {
                        Text(
                            text = stringResource(id = R.string.connessione_persa),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(LargePadding)
                        )
                    }
                }
            }
        }
    }
}
fun gestisciErrori(
    nome:String,
    cognome: String,
    codCliente:String,
    dataN:String,
    viewModel: AshbornViewModel
) {
    viewModel.setErroreNomeX(!viewModel.formatoNomeValido(nome))
    viewModel.setErroreCognomeX(!viewModel.formatoCognomeValido(cognome))
    viewModel.setErroreCodClienteX(!viewModel.formatoCodiceCliente(codCliente))
    viewModel.setErroreDataNX(!viewModel.formatoDataNascitaValida(dataN))
}
/*@Preview(showBackground = true)
@Composable
 fun Preview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            //Registrazione(navController, viewModel)
            /*AskPIN(
                navController = navController,
                viewModel = viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )*/
            Welcome(
                navController = navController,
                viewModel = viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )
        }
    }
}
*/

