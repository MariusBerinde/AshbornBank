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
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun DettagliOperazione(
    indexOperation: Long,
    navController: NavHostController,
    viewModel: AshbornViewModel
) {
    Log.i("Dettagli operazione", "indice operazione $indexOperation")
    var op=viewModel.operazioniConto.find { e->e.id==indexOperation}
    Log.i("Dettagli operazione", "Oggetto corrispondente ${op}")
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
                        Text(text = "Tipo di Operazione: ${op!!.operationType}", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ammontare: ${op!!.amount}", fontSize = 24.sp)
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
                    Text(
                        text = stringResource(id = R.string.dataO) + op!!.dateO.toLocalDate()
                            .toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.descrizione) + op.description,
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
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            onClick = { /* Azione delete */ }) {
                            Text(
                                //TODO true se non ancora eseguita false altrimenti
                                if (true) {
                                    stringResource(id = R.string.revoca)
                                } else {
                                    stringResource(id = R.string.disconosci)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}