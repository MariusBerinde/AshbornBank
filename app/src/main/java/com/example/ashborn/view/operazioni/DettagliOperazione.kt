package com.example.ashborn.view.operazioni

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.viewModel.DettagliOperazioneViewModel
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DettagliOperazione(
    operation: Operation,
    navController: NavHostController,
    viewModel: DettagliOperazioneViewModel,
) {
    /*Log.i("Dettagli operazione", "indice operazione $indexOperation")
    var op=viewModel.operazioniConto.find { e->e.id==indexOperation}
    Log.i("Dettagli operazione", "Oggetto corrispondente ${op}")*/
    Log.d("DettagliOperazione","Operazione = $operation")
    val nameFun = object {}.javaClass.enclosingMethod?.name
    Log.d(nameFun, "operazione arrivata: $operation")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Custom Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.dettagli_operazione),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Riquadro A: Centrato nella pagina
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Rettangolo per tipoOperazione
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                            .background(Color.LightGray) // Optional: background color
                    ) {
                        //Text(text = "Tipo di Operazione: ${op!!.operationType}", fontSize = 20.sp)
                        Text(text = "Tipo di Operazione: ${operation.operationType}", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    //Text(text = "Ammontare: ${op!!.amount}", fontSize = 24.sp)
                }
            }
            // Riquadro B: Contiene Data operazione, descrizione e pulsanti share e delete
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                Column {
                    //TODO: mettere voci in grassetto sistemare tipo operazione implementare pulsanti correttamente
                    Text(
                        //text = stringResource(id = R.string.dataO) + op!!.dateO.toLocalDate().toString(),
                        text = stringResource(id = R.string.dataO) + ": \n"+ operation.dateO.toLocalDate().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        //text = stringResource(id = R.string.descrizione) + op.description,
                        text = stringResource(id = R.string.descrizione) + "\n" + operation.description,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "share",
                                tint = Color.Gray
                            )
                        }
                        val todatAtFivePm =  LocalDateTime.of(
                            LocalDate.now().year,
                            LocalDate.now().month,
                            LocalDate.now().dayOfMonth,
                            17,
                            0,
                            0)
                        val dateIsTodayInTime = operation.dateO <= todatAtFivePm && LocalDateTime.now() <= todatAtFivePm &&
                                operation.operationStatus == OperationStatus.PENDING

                        val dateIsAtLeastTomorrow = operation.dateO >= todatAtFivePm &&
                                operation.operationStatus == OperationStatus.PENDING

                        if (dateIsTodayInTime || dateIsAtLeastTomorrow) {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Red),
                                onClick = {
                                    if(operation.operationStatus == OperationStatus.PENDING){
                                        val json = Json{prettyPrint=true}
                                        val data = json.encodeToString(Operation.serializer(), operation)
                                        navController.navigate("annulla-operazione/$data")
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.revoca)
                                )
                            }
                        } else {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Red),
                                onClick = {

                                        navController.navigate("disconosci-operazione")
                                 }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.disconosci)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}