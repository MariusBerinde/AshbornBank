package com.example.ashborn.view.operazioni

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.OperationType
import com.example.ashborn.data.TransactionType
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.view.CustomDatePickerDialog
import com.example.ashborn.view.DateUseCase
import com.example.ashborn.viewModel.MavViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun Mav(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(LargePadding),
        ) {
            Button(
                onClick = { navController.navigate("scan-qrcode") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.scan_qrcode))
            }
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(LargePadding)
        ) {
            Button(
                onClick = { navController.navigate("mav-manuale") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.inserisci_a_mano))
            }
        }

    }
}


@Composable
fun MavQrCode(
    navController: NavHostController,
    viewModel: MavViewModel,
) {
    val nameFun = object {}.javaClass.enclosingMethod?.name
    val barcodeResults = viewModel.barcodeResults.collectAsState(null)
    val json = Json { prettyPrint = true }

    ScanBarcode(
        viewModel.barcodeScanner::startScan,
        barcodeResults.value,
    )

    Log.d(nameFun,"risultato scan ${barcodeResults.value}")
    if (!barcodeResults.value.isNullOrBlank()) {

        Log.d(nameFun,"risultato scan valido=${barcodeResults.value}")
        val arguments = barcodeResults.value?.split("|") ?: emptyList()
        if (arguments.size == 3) {
            val operation = Operation(
                clientCode = viewModel.codCliente,
                dateO = viewModel.dataAccreditoMav,
                dateV = viewModel.dataAccreditoMav,
                transactionType = TransactionType.WITHDRAWAL,
                operationType = OperationType.MAV,
                amount = arguments[2].toDouble() / 100.0,
                bankAccount = viewModel.codConto,
                description = arguments[1],
                cardCode = null,
                iban = arguments[0],
                recipient = viewModel.codiceMav,
            )

            val data = Json { prettyPrint = true }.encodeToString(Operation.serializer(), operation)
            Log.d(nameFun, "Operazione creata: $operation")
            val encodedOperation = Uri.encode(data)
            
            navController.navigate("mav-manuale?operazione=$encodedOperation")
        } else {
            Log.d(nameFun, "initiateScanner: ${barcodeResults.value}")
            navController.popBackStack()
        }
    }
}
@Composable
private fun ScanBarcode(
    onScanBarcode: suspend () -> Unit,
    barcodeValue: String?,
) {
    val scope = rememberCoroutineScope()
    val nameFun = object {}.javaClass.enclosingMethod?.name
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth(.85f),
            onClick = {
                scope.launch {
                    onScanBarcode()
                }
            }) {
            Text(
                text = "Scan Barcode",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
            )
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MavManuale(
    navController: NavHostController,
    viewModel: MavViewModel,
    operation: Operation?,
) {
    val isOpen = remember { mutableStateOf(false) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val nameFun = object {}.javaClass.enclosingMethod?.name
    val focusManager = LocalFocusManager.current
    val json = Json { prettyPrint = true }
    if(operation != null) {
        viewModel.setCodiceMavX(operation.iban)
        viewModel.setImportoMavX(operation.amount.toString())
        viewModel.setDescrizioneMavX(operation.description)
    }
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
                if(operation == null)
                    navController.popBackStack()
                else
                    navController.navigate("mav")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = stringResource(id = R.string.mav),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.padding(LargePadding))
        Row() {
            var expanded by remember { mutableStateOf(false) }
            val selectedText = viewModel.ordinanteMav
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = viewModel.ordinanteMav,
                    onValueChange = { viewModel.setOrdinanteMavX(selectedText) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text(stringResource(id = R.string.ordinante)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.listaConti.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.codConto) },
                            onClick = {
                                //selectedText = item.codConto
                                viewModel.setOrdinanteMavX(item.codConto)
                                expanded = false
                            }
                        )
                    }
                }
            }

        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.dataAccredito)) },
                value = viewModel.dataAccreditoMav.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                onValueChange = { viewModel.setDataAccreditoMavX(viewModel.dataAccreditoMav) },
                trailingIcon = {
                    IconButton(onClick = { isOpen.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = stringResource(id = R.string.scegliData),
                            tint = Color.Black
                        )
                    }
                },
            )
            if (isOpen.value) {
                CustomDatePickerDialog(
                    yearRange = LocalDate.now().year..LocalDate.now().plusMonths(18).year,
                    onAccept = {
                        isOpen.value = false // close dialog
                        if (it != null) { // Set the date
                            viewModel.setDataAccreditoMavX(
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
                    useCase = DateUseCase.MAV
                )
            }
            // CustomDatePicker(useCase = DateUseCase.MAV, yearRange = LocalDate.now().year..LocalDate.now().plusYears(2).year)
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester1),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester2.requestFocus() }
                ),
                label = { Text(stringResource(id = R.string.codice_mav)) },
                value = viewModel.codiceMav,
                onValueChange = { viewModel.setCodiceMavX(it) },

                )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester2),
                //keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester3.requestFocus() }
                ),
                label = { Text(stringResource(id = R.string.importo)) },
                value = if(!viewModel.importoMav.contains(".")) viewModel.importoMav+".00" else viewModel.importoMav,
                onValueChange = { viewModel.setImportoMavX(it) }
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester3),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val limit_today = LocalDateTime.of( LocalDate.now().year, LocalDate.now().month, LocalDate.now().dayOfMonth, 17, 0, 0)
                        val operation = Operation(
                            clientCode = viewModel.codCliente,
                            dateO = viewModel.dataAccreditoMav,
                            dateV = viewModel.dataAccreditoMav,
                            transactionType = TransactionType.WITHDRAWAL,
                            operationType = OperationType.MAV,
                            amount = viewModel.importoMav.toDouble(),
                            bankAccount = viewModel.codConto,
                            description = viewModel.descrizioneMav,
                            cardCode = null,
                            iban = viewModel.codiceMav,
                            recipient = viewModel.codiceMav,
//                            operationStatus = if (viewModel.dataAccreditoMav == LocalDateTime.now()) OperationStatus.DONE else OperationStatus.PENDING,

                            operationStatus = if(viewModel.dataAccreditoMav <= limit_today)  OperationStatus.DONE else OperationStatus.PENDING,
                        )
                        val data = json.encodeToString(Operation.serializer(), operation)

                        Log.d(nameFun, "Operazione creata: $operation")

                        navController.navigate("riepilogo-operazione/$data")
                    }
                ),
                label = { Text(stringResource(id = R.string.descrizione)) },
                value = viewModel.descrizioneMav,
                onValueChange = { viewModel.setDescrizioneMavX(it) },
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val limit_today = LocalDateTime.of( LocalDate.now().year, LocalDate.now().month, LocalDate.now().dayOfMonth, 17, 0, 0)
                val operation = Operation(
                    clientCode = viewModel.codCliente,
                    dateO = viewModel.dataAccreditoMav,
                    dateV = viewModel.dataAccreditoMav,
                    transactionType = TransactionType.WITHDRAWAL,
                    operationType = OperationType.MAV,
                    amount = viewModel.importoMav.toDouble(),
                    bankAccount = viewModel.codConto,
                    description = viewModel.descrizioneMav,
                    cardCode = null,
                    iban = viewModel.codiceMav,
                    recipient = viewModel.codiceMav,
                    operationStatus = if(viewModel.dataAccreditoMav <= limit_today)  OperationStatus.DONE else OperationStatus.PENDING,
                )

                val data = json.encodeToString(Operation.serializer(), operation)

                Log.d(nameFun, "Operazione creata: $operation")
                navController.navigate("riepilogo-operazione/$data")
            }
        ) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}