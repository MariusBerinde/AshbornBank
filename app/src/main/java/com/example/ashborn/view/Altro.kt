package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ashborn.MainActivity
import com.example.ashborn.R
import com.example.ashborn.data.Voice
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AltroViewModel
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Altro(
    navController: NavHostController,
    viewModel: AltroViewModel
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .padding(MediumPadding)
                    .fillMaxWidth()
                    .padding(MediumPadding)
            ) {
                Row {
                    Column(modifier = Modifier.padding(MediumPadding)) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Blue)
                                .padding(MediumPadding)
                        ) {
                            val text = if(viewModel.userName.isEmpty()) "" else viewModel.userName[0].toString() + if(viewModel.cognome.isEmpty()  ) "" else viewModel.cognome[0].toString()
                            Text(
                                text = text,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(20.dp),
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column {
                        Text(stringResource(id = R.string.profilo))
                        //Text(viewModel.userName)
                        Text(stringResource(id = R.string.app_name))

                    }
                    Column(modifier = Modifier
                        .padding(horizontal = SmallPadding, vertical = 50.dp)
                        .clickable {
                            navController.navigate("utente")
                        }) {
                        Text("Dettagli", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Row {
            Column(modifier = Modifier.padding(SmallPadding)) {
                ListaAzioni(navController, viewModel)
            }
        }
    }
}


@Composable
fun ListaAzioni(navController: NavHostController, viewModel: AltroViewModel) {
    Column(modifier = Modifier.run {
        padding(SmallPadding)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
            .fillMaxWidth()
            .height(450.dp)
    }
    ) {
        val isOpen = remember { mutableStateOf(false) }
        var confermaEliminazioneSmartphone = remember { mutableStateOf(false) }

        val voci: ArrayList<Voice> = arrayListOf(
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.avvisi), "avvisi"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.archivio), "archivio"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.sicurezza), "sicurezza"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.impostazioni), "impostazioni"),
        )
        for (i in voci) {
            Card (
                modifier = Modifier
                    .padding(SmallPadding)
                    .height(60.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        Log.i("OperazioniAltro", i.name)
                        navController.navigate(i.destination)
                    }
                ) {
                    Icon(i.icon, contentDescription = "icona")
                    Text(i.name)
                }
            }
        }
        Card (
            modifier = Modifier
                .padding(SmallPadding)
                .height(60.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ){
            Row(modifier = Modifier
                .fillMaxSize()
                .clickable {
                    Log.d("OperazioniAltro", "elimina")
                    isOpen.value = !isOpen.value
                }
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "icona")
                Text(stringResource(id = R.string.elimina))
            }
        }
        if (isOpen.value) {
            Dialog(onDismissRequest = {isOpen.value = false}) {
                Card (
                    modifier = Modifier
                        .padding(MediumPadding)
                        .fillMaxWidth()
                ){
                    Column (
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        verticalArrangement = Arrangement.Center
                    ){
                        Row (
                            modifier = Modifier
                                .padding(LargePadding)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = stringResource(id = R.string.elimina_dialog))
                        }
                        Row (
                            modifier = Modifier
                                .padding(SmallPadding)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { isOpen.value = false}) {
                                Text(text = stringResource(id = R.string.annulla))
                            }
                            Spacer(modifier = Modifier.padding(MediumPadding))
                            Button(
                                onClick = {
                                    confermaEliminazioneSmartphone.value = true
                                    isOpen.value = false
                                }
                            ){
                                Text(text = stringResource(id = R.string.conferma))
                            }
                        }
                    }
                }
            }
        }

        if (confermaEliminazioneSmartphone.value) {
            Dialog(
                onDismissRequest = {
                    isOpen.value = false
                    confermaEliminazioneSmartphone.value = false
                }
            ) {
                Card (
                    modifier = Modifier
                        .padding(MediumPadding)
                        .fillMaxWidth()
                ){
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalArrangement = Arrangement.Center
                    ){
                        Row (
                            modifier = Modifier
                                .padding(LargePadding)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = stringResource(id = R.string.conferma_elimina_dialog))
                        }
                        Row (
                            modifier = Modifier
                                .padding(SmallPadding)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    isOpen.value = false
                                    confermaEliminazioneSmartphone.value = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.annulla))
                            }
                            Spacer(modifier = Modifier.padding(MediumPadding))
                            Button(
                                onClick = {
                                    viewModel.deletePreferences()
                                    navController.navigate("welcome")
                                },
                                colors = ButtonColors(Color.Red, Color.Black, Color.Red, Color.Black)
                            ){
                                Text(text = stringResource(id = R.string.conferma))
                            }
                        }
                    }
                }
            }
        }
    }
}