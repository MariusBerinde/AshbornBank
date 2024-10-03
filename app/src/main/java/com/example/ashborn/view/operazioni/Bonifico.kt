package com.example.ashborn.view.operazioni

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.Validatore
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.OperationType
import com.example.ashborn.data.TransactionType
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.view.CustomDatePickerDialog
import com.example.ashborn.view.DateUseCase
import com.example.ashborn.view.login.StatoErrore
import com.example.ashborn.viewModel.BonificoViewModel
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bonifico(
    navController: NavHostController,
    viewModel: BonificoViewModel,
){
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val isOpen = remember { mutableStateOf(false) }
    val isChecked = remember { mutableStateOf(false) }
    val nameFun = object {}.javaClass.enclosingMethod?.name
    val dest =  integerResource(R.integer.Operazioni)
    val isDark = isSystemInDarkTheme()
    BackHandler(enabled = true) { navController.navigate("conti?index=$dest") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.navigate("conti?index=$dest") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                modifier = Modifier.padding(start = LargePadding * 4),
                text = stringResource(id = R.string.bonifico),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
            )
        }

        Column {
            val modifier = Modifier
                .padding(SmallPadding)
                .fillMaxWidth()
            Row {
                var expanded by remember { mutableStateOf(false) }
                val selectedText = viewModel.codConto
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = viewModel.codConto,
                        onValueChange = { viewModel.setCodContoX(selectedText) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .padding(SmallPadding)
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text(stringResource(id = R.string.ordinante)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if(isDark) Color.White
                                                 else Color.Black,
                            unfocusedBorderColor = if(isDark) Color.White
                                                   else Color.Black,
                        ),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        viewModel.listaConti.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.codConto) },
                                onClick = {
                                    viewModel.setCodContoX(item.codConto)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.padding(top = MediumPadding, bottom = SmallPadding))
            Row {
                val maxLength = 100
                OutlinedTextField(
                    value = viewModel.beneficiario,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            viewModel.setBeneficiarioX(it)
                        }
                    },
                    label = { Text(stringResource(id = R.string.beneficiario)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.erroreBeneficiario != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,
                        unfocusedBorderColor = if (viewModel.erroreBeneficiario != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,

                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester2.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester1)
                        .padding(SmallPadding)
                        .fillMaxWidth(),
                    singleLine = true,
                )
            }
            Row(
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxWidth()
                    .height(LargePadding),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = when(viewModel.erroreBeneficiario){
                        StatoErrore.NESSUNO, StatoErrore.CONTENUTO -> ""
                        StatoErrore.FORMATO -> stringResource(id = R.string.errore_beneficiario)
                    },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                )
            }
            Row {
                val maxLength = 27
                OutlinedTextField(
                    value = viewModel.iban,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            viewModel.setIbanX(it)
                        }
                    },
                    label = { Text(stringResource(id = R.string.iban)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.erroreIban != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,
                        unfocusedBorderColor = if (viewModel.erroreIban != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester3.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester2)
                        .padding(SmallPadding)
                        .fillMaxWidth(),
                    singleLine = true,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SmallPadding)
                    .height(LargePadding),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = when(viewModel.erroreIban) {
                        StatoErrore.NESSUNO, StatoErrore.CONTENUTO -> ""
                        StatoErrore.FORMATO -> stringResource(id = R.string.errore_iban)
                    },
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row {
                OutlinedTextField(
                    value = if(!viewModel.importo.contains(".") && viewModel.importo.isNotBlank())
                            viewModel.importo + ".00"
                        else if (viewModel.importo == ".00") ""
                        else viewModel.importo,
                    onValueChange = { viewModel.setImportoX(it) },
                    label = { Text(stringResource(id = R.string.importo)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.erroreImporto != StatoErrore.NESSUNO)
                                                 Color.Red
                                             else if (isDark)
                                                 Color.White
                                             else
                                                 Color.Black,
                        unfocusedBorderColor = if (viewModel.erroreImporto != StatoErrore.NESSUNO)
                                                   Color.Red
                                               else if (isDark)
                                                   Color.White
                                               else
                                                   Color.Black,
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
                        .fillMaxWidth(),
                    singleLine = true,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SmallPadding)
                    .height(LargePadding),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when(viewModel.erroreImporto) {
                        StatoErrore.NESSUNO, StatoErrore.CONTENUTO ->  ""
                        StatoErrore.FORMATO -> stringResource(id = R.string.errore_importo)
                    },
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row {
                val maxLength = 300
                OutlinedTextField(
                    value = viewModel.causale,
                    onValueChange = {
                        if (it.length <= maxLength) { viewModel.setCausaleX(it) }
                    },
                    label = { Text(stringResource(id = R.string.causale)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.erroreCausale != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,
                        unfocusedBorderColor = if (viewModel.erroreCausale != StatoErrore.NESSUNO)
                                Color.Red
                            else if (isDark)
                                Color.White
                            else
                                Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester5.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester4)
                        .padding(SmallPadding)
                        .fillMaxWidth(),
                    singleLine = true,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SmallPadding)
                    .height(LargePadding),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = when(viewModel.erroreImporto){
                        StatoErrore.NESSUNO,StatoErrore.CONTENUTO -> ""
                        StatoErrore.FORMATO -> stringResource(id = R.string.errore_causale)
                    },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                )
            }
            Row {
                OutlinedTextField(
                    readOnly = true,
                    value = (viewModel.dataAccredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()),
                    onValueChange = { viewModel.setDataAccreditoX(viewModel.dataAccredito) },
                    label = { Text(stringResource(id = R.string.dataAccredito)) },
                    trailingIcon = {
                        IconButton(onClick = { isOpen.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = stringResource(id = R.string.scegliData),
                                tint = Color.Black
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (viewModel.erroreDataAccredito != StatoErrore.NESSUNO)
                            Color.Red
                        else if (isDark)
                            Color.White
                        else
                            Color.Black,
                        unfocusedBorderColor = if (viewModel.erroreDataAccredito != StatoErrore.NESSUNO)
                                                   Color.Red
                                               else if (isDark)
                                                   Color.White
                                               else
                                                   Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (Validatore().formatoBonificoValido(
                                    viewModel.beneficiario,
                                    viewModel.iban,
                                    viewModel.importo,
                                    viewModel.causale,
                                    viewModel.dataAccredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                )
                            ) {
                                Log.i("Bonifico", "Sto per andare in riepilogo")
                                val operation = Operation(
                                    clientCode = viewModel.codCliente,
                                    dateO = viewModel.dataAccredito,
                                    dateV = viewModel.dataAccredito,
                                    transactionType = TransactionType.WITHDRAWAL,
                                    operationType = if(isChecked.value) OperationType.INSTANTANEOUS_WIRE_TRANSFER else OperationType.WIRE_TRANSFER,
                                    amount = viewModel.importo.toDouble(),
                                    bankAccount = viewModel.codConto,
                                    description = viewModel.causale,
                                    cardCode = null,
                                    iban = viewModel.iban,
                                    recipient = viewModel.beneficiario,
                                    operationStatus = if(isChecked.value) OperationStatus.DONE else OperationStatus.PENDING
                                )

                                val json = Json { prettyPrint = true }
                                val data = json.encodeToString(Operation.serializer(), operation)
                                Log.d(nameFun, "Operazione creata: $operation")
                                navController.navigate("riepilogo-operazione/$data")
                            } else {
                                gestisciErroriBonifico(
                                    viewModel.beneficiario,
                                    viewModel.iban,
                                    viewModel.importo,
                                    viewModel.causale,
                                    viewModel.dataAccredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    viewModel
                                )
                            }
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester5)
                        .padding(SmallPadding)
                        .fillMaxWidth(),
                    singleLine = true,

                )
                if (isOpen.value) {
                    CustomDatePickerDialog(
                        yearRange = LocalDate.now().year..LocalDate.now().plusYears(1).year,
                        onAccept = {
                            isOpen.value = false // close dialog
                            if (it != null) { // Set the date
                                viewModel.setDataAccreditoX(
                                    Instant
                                        .ofEpochMilli(it)
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDateTime()
                                )
                            }
                        },
                        onCancel = {
                            isOpen.value = false //close dialog
                        },
                        useCase = DateUseCase.BONIFICO
                    )
                }
            }
            Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.bonifico_istantaneo),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.padding(SmallPadding))
                Switch(
                    checked = isChecked.value,
                    onCheckedChange = {isChecked.value = !isChecked.value}
                )
            }
            Row {
                Button(
                    onClick = {
                        if (Validatore().formatoBonificoValido(
                                viewModel.beneficiario,
                                viewModel.iban,
                                viewModel.importo,
                                viewModel.causale,
                                viewModel.dataAccredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            )
                        ) {
                            val operation = Operation(
                                clientCode = viewModel.codCliente,
                                dateO = viewModel.dataAccredito,
                                dateV = viewModel.dataAccredito,
                                transactionType = TransactionType.WITHDRAWAL,
                                operationType = if(isChecked.value) OperationType.INSTANTANEOUS_WIRE_TRANSFER else OperationType.WIRE_TRANSFER,
                                amount = viewModel.importo.toDouble(),
                                bankAccount = viewModel.codConto,
                                description = viewModel.causale,
                                cardCode = null,
                                iban = viewModel.iban,
                                recipient = viewModel.beneficiario,
                                operationStatus = if(isChecked.value) OperationStatus.DONE else OperationStatus.PENDING,
                            )
                            val json = Json { prettyPrint = true }
                            val data = json.encodeToString(Operation.serializer(), operation)
                            Log.d(nameFun, "Operazione creata: $operation")
                            navController.navigate("riepilogo-operazione/$data")
                        } else {
                            gestisciErroriBonifico(
                                viewModel.beneficiario,
                                viewModel.iban,
                                viewModel.importo,
                                viewModel.causale,
                                viewModel.dataAccredito.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                viewModel
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