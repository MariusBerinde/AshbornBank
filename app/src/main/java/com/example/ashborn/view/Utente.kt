package com.example.ashborn.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.UtenteViewModel

@Composable
fun Utente(
    viewModel: UtenteViewModel,
    navController: NavHostController,
) {
//todo:Migliorare la grafica della pagina
    val dest = integerResource(R.integer.Altro)
    BackHandler(enabled = true) { navController.navigate("conti?index=$dest") }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { navController.navigate("conti?index=$dest") },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.padding(LargePadding*2))
            Text(
                stringResource(id = R.string.utente),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.padding(MediumPadding))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LargePadding),
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(LargePadding)
                ) {
                    val modifier = Modifier
                        .fillMaxWidth()
                        .padding(LargePadding)
                        .align(Alignment.CenterHorizontally)
                    Row(modifier = modifier) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.nome)
                        )
                        Text(text = viewModel.userName)
                    }
                    Row(modifier = modifier) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.cognome)
                        )
                        Text(text = viewModel.cognome)
                    }
                    Row(modifier = modifier) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.data_nascita) + ": "
                        )
                        Text(text = viewModel.dataNascita)
                    }
                    Row(modifier = modifier) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.codice_cliente)
                        )
                        Text(text = viewModel.codCliente)
                    }
                }
            }
        }
    }


}



