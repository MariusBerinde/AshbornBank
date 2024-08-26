package com.example.ashborn.view.operazioni

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationType
import com.example.ashborn.ui.theme.LargePadding
import kotlinx.serialization.json.Json
import java.time.format.DateTimeFormatter

@Composable
fun RiepilogoOperazione(navController : NavHostController, operation: Operation){
    Log.i("RiepilogoBonifico", "Scelta operazione con id")
    val modifier = Modifier.padding(LargePadding)
    Column(modifier = Modifier.padding(LargePadding)) {
        Text(
            text = stringResource(
                id = when(operation.operationType){
                    OperationType.MAV -> R.string.riepilogo_mav
                    OperationType.WIRE_TRANSFER -> R.string.bonifico
                    OperationType.CARD -> R.string.carta
                }
            ),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = modifier)

        when(operation.operationType){
            OperationType.MAV, OperationType.WIRE_TRANSFER-> {
                Text(text = stringResource(id = R.string.ordinante), fontWeight = FontWeight.Bold)
                Text(text = operation.bankAccount)
                Spacer(modifier = modifier)
            }
            OperationType.CARD -> { Spacer(modifier = modifier) }
        }

        when(operation.operationType){
            OperationType.WIRE_TRANSFER-> {
                Text(text = stringResource(id = R.string.beneficiario), fontWeight = FontWeight.Bold)
                Text(text = operation.recipient)
                Spacer(modifier = modifier)
            }
            OperationType.MAV, OperationType.CARD -> { Spacer(modifier = modifier) }
        }
        when(operation.operationType){
            OperationType.WIRE_TRANSFER-> {
                Text(text = stringResource(id = R.string.iban), fontWeight = FontWeight.Bold)
            }
            OperationType.MAV -> {
                Text(text = stringResource(id = R.string.codice_mav), fontWeight = FontWeight.Bold)
            }
            OperationType.CARD -> {
                Text(text = stringResource(id = R.string.iban), fontWeight = FontWeight.Bold)
            }
        }
        Text(text = operation.iban)
        Spacer(modifier = modifier)

        Text(text = stringResource(id = R.string.importo), fontWeight = FontWeight.Bold)
        Text(text = operation.amount.toString())
        Spacer(modifier = modifier)

        Text(text = stringResource(id = R.string.causale), fontWeight = FontWeight.Bold)
        Text(text = operation.description)
        Spacer(modifier = modifier)

        Text(text = stringResource(id = R.string.dataAccredito), fontWeight = FontWeight.Bold)
        Text(text = operation.dateO.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        Spacer(modifier = modifier)

        Button(
            onClick = {
                val json = Json{prettyPrint=true}
                val data = json.encodeToString(Operation.serializer(), operation)
                navController.navigate("pin/$data")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}