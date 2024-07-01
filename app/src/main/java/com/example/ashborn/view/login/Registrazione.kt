package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.Validatore
import com.example.ashborn.data.User
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.view.ErroreConnessione
import com.example.ashborn.viewModel.AshbornViewModel

//todo: creare file separato
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
    val coroutineScope = rememberCoroutineScope()
    ErroreConnessione(connectionStatus = connectionStatus) {
        Column(
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
                onValueChange = { viewModel.setUserNameX(it) },
                label = { Text(stringResource(id = R.string.inserisci_nome)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.erroreNome == 1) {
                        Color.Red
                    } else {
                        Color.Black
                    },
                    unfocusedBorderColor = if (viewModel.erroreNome == 1) {
                        Color.Red
                    } else {
                        Color.Black
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
                onValueChange = { viewModel.setCognomeX(it) },
                label = { Text(stringResource(R.string.inserisci_cognome)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.erroreCognome == 1) {
                        Color.Red
                    } else {
                        Color.Black
                    },
                    unfocusedBorderColor = if (viewModel.erroreCognome == 1) {
                        Color.Red
                    } else {
                        Color.Black
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
                onValueChange = { viewModel.setDataNascitaX(it) },
                label = { Text(stringResource(id = R.string.ins_data_nascita)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.erroreDataNascita == 1) {
                        Color.Red
                    } else {
                        Color.Black
                    },
                    unfocusedBorderColor = if (viewModel.erroreDataNascita == 1) {
                        Color.Red
                    } else {
                        Color.Black
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
                onValueChange = { viewModel.setCodClienteX(it) },
                label = { Text(stringResource(R.string.inserisci_codice_cliente)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (viewModel.erroreCodCliente == 1) {
                        Color.Red
                    } else {
                        Color.Black
                    },
                    unfocusedBorderColor = if (viewModel.erroreCodCliente == 1) {
                        Color.Red
                    } else {
                        Color.Black
                    }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (
                            Validatore().formatoNomeValido(viewModel.userName) &&
                            Validatore().formatoDataNascitaValida(viewModel.dataNascita) &&
                            Validatore().formatoCognomeValido(viewModel.cognome) &&
                            Validatore().formatoCodiceCliente(viewModel.codCliente)
                        ) {
                            viewModel.fistLogin = false

                            if (viewModel.validUser(
                                    User(
                                        viewModel.userName,
                                        viewModel.cognome,
                                        viewModel.dataNascita,
                                        "",
                                        viewModel.codCliente,
                                    )
                                )
                            ) {
                                Log.i("Registrazione", "sono in onsend con valid user")
                                viewModel.writePreferences()
                                navController.navigate("welcome")
                            } else {
                                Log.i("Registrazione", "sono in onsend else")
                                gestisciErrori(
                                    viewModel.userName,
                                    viewModel.cognome,
                                    viewModel.codCliente,
                                    viewModel.dataNascita, viewModel
                                )
                            }
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
                        Validatore().formatoNomeValido(viewModel.userName) &&
                        Validatore().formatoDataNascitaValida(viewModel.dataNascita) &&
                        Validatore().formatoCognomeValido(viewModel.cognome) &&
                        Validatore().formatoCodiceCliente(viewModel.codCliente)
                    ) {
                        viewModel.fistLogin = false

                        if (viewModel.validUser(
                                User(
                                    viewModel.userName,
                                    viewModel.cognome,
                                    viewModel.dataNascita,
                                    "",
                                    viewModel.codCliente
                                )
                            )
                        ) {

                            Log.i("Registrazione", "sono in onclick con valid user")
                            viewModel.writePreferences()
                            navController.navigate("welcome")
                        } else {

                            Log.i("Registrazione", "sono in onclick else")
                            gestisciErrori(
                                viewModel.userName,
                                viewModel.cognome,
                                viewModel.codCliente,
                                viewModel.dataNascita, viewModel
                            )
                        }
                    } else {
                        gestisciErrori(
                            viewModel.userName,
                            viewModel.cognome,
                            viewModel.codCliente,
                            viewModel.dataNascita, viewModel
                        )
                    }
                }) {
                Text(text = stringResource(id = R.string.conferma))
            }
        }
    }
}