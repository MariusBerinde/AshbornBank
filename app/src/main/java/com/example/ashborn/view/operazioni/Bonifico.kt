package com.example.ashborn.view.operazioni

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.Validatore
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.OperationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bonifico(navController: NavHostController, viewModelOp: OperationViewModel){
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }

        Text(text = "Bonifico")
        Column() {
            val modifier = Modifier
                .padding(SmallPadding)
                .fillMaxWidth()
            Row() {
                OutlinedTextField(
                    value = viewModelOp.codConto,
                    onValueChange = {},
                    modifier = modifier,
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.ordinante)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )
            }
            Row() {
                val maxLength = 100
                OutlinedTextField(
                    value = viewModelOp.beneficiario,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            viewModelOp.setBeneficiarioX(it)
                        }
                    },
                    label = { Text(stringResource(id = R.string.beneficiario)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModelOp.erroreBeneficiario == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        },
                        unfocusedBorderColor = if (viewModelOp.erroreBeneficiario == 1) {
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
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row() {
                val maxLength = 27
                OutlinedTextField(
                    value = viewModelOp.iban,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            viewModelOp.setIbanX(it)
                        }
                    },
                    label = { Text(stringResource(id = R.string.iban)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModelOp.erroreIban == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        },
                        unfocusedBorderColor = if (viewModelOp.erroreIban == 1) {
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
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row() {
                OutlinedTextField(
                    value = viewModelOp.importo,
                    onValueChange = { viewModelOp.setImportoX(it) },
                    label = { Text(stringResource(id = R.string.importo)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModelOp.erroreImporto == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        },
                        unfocusedBorderColor = if (viewModelOp.erroreImporto == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester4.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester3)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row() {
                val maxLength = 300
                OutlinedTextField(
                    value = viewModelOp.causale,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            viewModelOp.setCausaleX(it)
                        }
                    },
                    label = { Text(stringResource(id = R.string.causale)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModelOp.erroreCausale == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        },
                        unfocusedBorderColor = if (viewModelOp.erroreCausale == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester5.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester4)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row() {
                OutlinedTextField(
                    value = viewModelOp.dataAccredito,
                    onValueChange = { viewModelOp.setDataAccreditoX(it) },
                    label = { Text(stringResource(id = R.string.dataAccredito)) },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO mostra date picker dialog*/ }) {
                            Icon(
                                Icons.Filled.DateRange,
                                contentDescription = stringResource(id = R.string.scegliData),
                                tint = Color.Black
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModelOp.erroreDataAccredito == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        },
                        unfocusedBorderColor = if (viewModelOp.erroreDataAccredito == 1) {
                            Color.Red
                        } else {
                            Color.Black
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (Validatore().formatoBonificoValido(
                                    viewModelOp.beneficiario,
                                    viewModelOp.iban,
                                    viewModelOp.importo,
                                    viewModelOp.causale,
                                    viewModelOp.dataAccredito
                                )
                            ) {
                                Log.i("Bonifico", "Sto per andare in riepilogo")
                                navController.navigate("riepilogo-bonifico")
                            } else {
                                gestisciErrori(
                                    viewModelOp.beneficiario,
                                    viewModelOp.iban,
                                    viewModelOp.importo,
                                    viewModelOp.causale,
                                    viewModelOp.dataAccredito,
                                    viewModelOp
                                )
                            }
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester5)
                        .padding(SmallPadding)
                        .fillMaxWidth()

                )
            }
            Row() {
                Button(
                    onClick = {
                        if (Validatore().formatoBonificoValido(
                                viewModelOp.beneficiario,
                                viewModelOp.iban,
                                viewModelOp.importo,
                                viewModelOp.causale,
                                viewModelOp.dataAccredito
                            )
                        ) {
                            Log.i("Bonifico", "Sto per andare in riepilogo")
                            navController.navigate("riepilogo-bonifico")
                        } else {
                            gestisciErrori(
                                viewModelOp.beneficiario,
                                viewModelOp.iban,
                                viewModelOp.importo,
                                viewModelOp.causale,
                                viewModelOp.dataAccredito,
                                viewModelOp
                            )
                        }
                    },
                    modifier = modifier
                ) {
                    Text(text = stringResource(id = R.string.continua))
                }
            }
        }
    }
}