package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.CarteViewModel
import com.example.ashborn.viewModel.ContiViewModel
import kotlinx.serialization.json.Json

@Composable
fun ListaOperazioniFatteConti(
    navController: NavHostController,
    viewModel: ContiViewModel,
) {
    val nameFun = object {}.javaClass.enclosingMethod?.name
    var ordineInversoData = remember { mutableStateOf(true) }
    var ordineInversoDescrizione = remember { mutableStateOf(true) }
    var ordineInversoImporto = remember { mutableStateOf(true) }
    var voci = viewModel.operazioni
    LazyColumn(
        modifier = Modifier
            .padding(SmallPadding)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
            .fillMaxWidth()
            .height(450.dp)
    ) {
        item {
            Row(modifier = Modifier.padding(SmallPadding)) {
                Button(
                    onClick = {
                        if (ordineInversoData.value) {
                            viewModel.operazioni =
                                ArrayList(voci?.sortedByDescending { it.dateO })
                            ordineInversoData.value = !ordineInversoData.value
                        } else {
                            viewModel.operazioni = ArrayList(voci?.sortedBy { it.dateO })
                            ordineInversoData.value = !ordineInversoData.value
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.data))
                    Icon(
                        imageVector = if (ordineInversoData.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoData.value) "ordine ascendente" else "ordine discendente"
                    )

                }
                Button(onClick = {
                    if (ordineInversoDescrizione.value) {
                        viewModel.operazioni =
                            ArrayList(voci?.sortedByDescending { it.description })
                        ordineInversoData.value = !ordineInversoData.value
                    } else {
                        viewModel.operazioni = ArrayList(voci?.sortedBy { it.description })
                        ordineInversoDescrizione.value = !ordineInversoDescrizione.value
                    }
                }) {
                    Text(text = stringResource(id = R.string.descrizione))
                    Icon(
                        imageVector = if (ordineInversoDescrizione.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoDescrizione.value) "ordine ascendente" else "ordine discendente"
                    )
                }
                Button(
                    onClick = {
                        if (ordineInversoImporto.value) {
                            viewModel.operazioni =
                                ArrayList(voci?.sortedByDescending { it.amount })
                            ordineInversoImporto.value = !ordineInversoImporto.value
                        } else {
                            viewModel.operazioni = ArrayList(voci?.sortedBy { it.amount })
                            ordineInversoImporto.value = !ordineInversoImporto.value
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.importo))
                    Icon(
                        imageVector = if (ordineInversoImporto.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoImporto.value) "ordine ascendente" else "ordine discendente"
                    )
                }
            }
        }
        if (voci == null) {
            item { Text(text = "non ci sono elementi") }
        } else {
            for (i in voci) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(SmallPadding)
                            .height(60.dp)
                            .fillMaxWidth()
                        //.align(Alignment.CenterHorizontally)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                Log.d(nameFun, "operazione inviata: ${i}")
                                val json = Json { prettyPrint = true }
                                val data = json.encodeToString(Operation.serializer(), i)

                                navController.navigate("dettagli-operazione/$data")
                            }
                        ) {
                            val modifier = Modifier.padding(16.dp, 18.dp, 2.dp, 4.dp)
                            Text(
                                text = i.dateO.toLocalDate().toString(),
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                            Text(
                                text = i.description,
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                            Text(
                                text = if (i.operationType == TransactionType.WITHDRAWAL) {
                                    "-"
                                } else {
                                    "+"
                                } + i.amount.toString() + "€",
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}
//TODO: Aggiusta navigazione back
@Composable
fun ListaOperazioniFatteCarte(
    navController: NavHostController,
    viewModel: CarteViewModel,
) {
    val nameFun = object {}.javaClass.enclosingMethod?.name
    var ordineInversoData = remember { mutableStateOf(true) }
    var ordineInversoDescrizione = remember { mutableStateOf(true) }
    var ordineInversoImporto = remember { mutableStateOf(true) }
    var voci = viewModel.operazioni
    LazyColumn(
        modifier = Modifier
            .padding(SmallPadding)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
            .fillMaxWidth()
            .height(450.dp)
    ) {
        item {
            Row(modifier = Modifier.padding(SmallPadding)) {
                Button(
                    onClick = {
                        if (ordineInversoData.value) {
                            viewModel.operazioni =
                                ArrayList(voci?.sortedByDescending { it.dateO })
                            ordineInversoData.value = !ordineInversoData.value
                        } else {
                            viewModel.operazioni = ArrayList(voci?.sortedBy { it.dateO })
                            ordineInversoData.value = !ordineInversoData.value
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.data))
                    Icon(
                        imageVector = if (ordineInversoData.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoData.value) "ordine ascendente" else "ordine discendente"
                    )

                }
                Button(onClick = {
                    if (ordineInversoDescrizione.value) {
                        viewModel.operazioni =
                            ArrayList(voci?.sortedByDescending { it.description })
                        ordineInversoData.value = !ordineInversoData.value
                    } else {
                        viewModel.operazioni = ArrayList(voci?.sortedBy { it.description })
                        ordineInversoDescrizione.value = !ordineInversoDescrizione.value
                    }
                }) {
                    Text(text = stringResource(id = R.string.descrizione))
                    Icon(
                        imageVector = if (ordineInversoDescrizione.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoDescrizione.value) "ordine ascendente" else "ordine discendente"
                    )
                }
                Button(
                    onClick = {
                        if (ordineInversoImporto.value) {
                            viewModel.operazioni =
                                ArrayList(voci?.sortedByDescending { it.amount })
                            ordineInversoImporto.value = !ordineInversoImporto.value
                        } else {
                            viewModel.operazioni = ArrayList(voci?.sortedBy { it.amount })
                            ordineInversoImporto.value = !ordineInversoImporto.value
                        }
                    }
                ) {
                    Text(text = stringResource(id = R.string.importo))
                    Icon(
                        imageVector = if (ordineInversoImporto.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (ordineInversoImporto.value) "ordine ascendente" else "ordine discendente"
                    )
                }
            }
        }
        if (voci == null) {
            item { Text(text = "non ci sono elementi") }
        } else {
            for (i in voci) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(SmallPadding)
                            .height(60.dp)
                            .fillMaxWidth()
                        //.align(Alignment.CenterHorizontally)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                Log.d(nameFun, "operazione inviata: ${i}")
                                val json = Json { prettyPrint = true }
                                val data = json.encodeToString(Operation.serializer(), i)

                                navController.navigate("dettagli-operazione/$data")
                            }
                        ) {
                            val modifier = Modifier.padding(16.dp, 18.dp, 2.dp, 4.dp)
                            Text(
                                text = i.dateO.toLocalDate().toString(),
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                            Text(
                                text = i.description,
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                            Text(
                                text = if (i.operationType == TransactionType.WITHDRAWAL) {
                                    "-"
                                } else {
                                    "+"
                                } + i.amount.toString() + "€",
                                fontSize = 13.sp,
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}