package com.example.ashborn.view.login

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.viewModel.NavigationEvent
import com.example.ashborn.R
import com.example.ashborn.RegistrazioneViewModel
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.view.CustomDatePickerDialog
import com.example.ashborn.view.DateUseCase
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun Registrazione(
    navController: NavHostController,
    viewModel: RegistrazioneViewModel,
) {
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val navigationState by viewModel.navigationState.observeAsState()
    val isOpen = remember { mutableStateOf(false) }
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
        )
        OutlinedTextField(
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.dataNascita,
            label = { Text(stringResource(id = R.string.data_nascita)) },
            onValueChange = {viewModel.setDataNascitaX(it)},
            trailingIcon = {
                IconButton(
                    onClick = { isOpen.value = true } // show the dialog
                ) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                }
            },
            singleLine = true,
        )
        if (isOpen.value) {
            CustomDatePickerDialog(
                yearRange = LocalDate.now().minusYears(100).year..LocalDate.now().year,
                onAccept = {
                    isOpen.value = false // close dialog
                    if (it != null) { // Set the date
                        viewModel.setDataNascitaX(
                            Instant
                                .ofEpochMilli(it)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        )
                    }
                },
                onCancel = {
                    isOpen.value = false //close dialog
                },
                useCase = DateUseCase.NASCITA
            )
        }
        Spacer(modifier = Modifier.height(LargePadding))
        val isDarkMode = isSystemInDarkTheme()
        OutlinedTextField(
            value = viewModel.userName,
            onValueChange = { viewModel.setUserNameX(it) },
            label = { Text(stringResource(id = R.string.inserisci_nome)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (viewModel.erroreNome != StatoErrore.NESSUNO) Color.Red
                                     else if(isDarkMode) Color.White
                                     else Color.Black,
                unfocusedBorderColor = if (viewModel.erroreNome != StatoErrore.NESSUNO) Color.Red
                                       else if(isDarkMode) Color.White
                                       else Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusRequester2.requestFocus() }
            ),
            modifier = Modifier
                .focusRequester(focusRequester1)
                .fillMaxWidth(),
            singleLine = true,
        )
        Text(
            text = when (viewModel.erroreNome) {
                StatoErrore.FORMATO -> {
                    stringResource(id = R.string.errore_nome)}
                StatoErrore.CONTENUTO -> {
                    stringResource(id = R.string.errore_nome_contenuto)}
                StatoErrore.NESSUNO -> ""
            },
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(LargePadding))
        OutlinedTextField(
            value = viewModel.cognome,
            onValueChange = { viewModel.setCognomeX(it) },
            label = { Text(stringResource(R.string.inserisci_cognome)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (viewModel.erroreCognome != StatoErrore.NESSUNO) {
                    Color.Red
                } else {
                    Color.Black
                },
                unfocusedBorderColor = if (viewModel.erroreCognome != StatoErrore.NESSUNO) {
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
                .fillMaxWidth(),
            singleLine = true,
        )
        Text(
            text = when (viewModel.erroreCognome) {
                StatoErrore.FORMATO -> {
                    stringResource(id = R.string.errore_conome)}
                StatoErrore.CONTENUTO -> {
                    stringResource(id = R.string.errore_conome_contenuto)}
                StatoErrore.NESSUNO -> ""
            },
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(LargePadding))
        val isDark = isSystemInDarkTheme()
        OutlinedTextField(
            value = viewModel.codCliente,
            onValueChange = { viewModel.setCodClienteX(it) },
            label = { Text(stringResource(R.string.inserisci_codice_cliente)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (viewModel.erroreCodCliente != StatoErrore.NESSUNO)
                    Color.Red
                else if(isDark)
                    Color.White
                else
                    Color.Black,
                unfocusedBorderColor = if (viewModel.erroreCodCliente != StatoErrore.NESSUNO)
                    Color.Red
                else if(isDark)
                    Color.White
                else
                    Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { viewModel.auth() }),
            modifier = Modifier
                .focusRequester(focusRequester3)
                .fillMaxWidth(),
            singleLine = true,
        )
        Text(
            text = when (viewModel.erroreCodCliente) {
                StatoErrore.FORMATO -> {
                    stringResource(id = R.string.errore_cod_cliente)}
                StatoErrore.CONTENUTO -> {
                    stringResource(id = R.string.errore_cod_cliente_contenuto)}
                StatoErrore.NESSUNO -> ""
            },
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(SmallPadding))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.auth() }
        ) {
            Text(text = stringResource(id = R.string.conferma))
        }


    }
    LaunchedEffect(navigationState) {
        when(navigationState){
            NavigationEvent.NavigateToPin ->{
                Log.i("Registrazione","sono in Launced effect con navState.toConti")
                viewModel.fistLogin= false
                viewModel.writePreferences()
                navController.navigate("welcome")
            }
            NavigationEvent.NavigateToError -> gestisciErrori(viewModel)
            else->{}

        }

    }
}

