package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Carta
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.CarteViewModel
import java.time.format.DateTimeFormatter

@Composable
fun Carte(
    navController: NavHostController,
    viewModel: CarteViewModel
) {
    Column (
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(
                modifier = Modifier.padding(top = 70.dp),
                onClick = { viewModel.cartaPrecedente() }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "precedente")
            }
            FronteCarta(viewModel.cartaMostrata,viewModel.userName + " " + viewModel.cognome)
            IconButton(
                modifier = Modifier.padding(top = 70.dp),
                onClick = { viewModel.cartaSuccessiva() }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "successivo")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column (modifier = Modifier.padding(SmallPadding)){
                ListaOperazioniFatteCarte(navController, viewModel)
            }
        }
    }
}

@Composable
fun RigaMagnetica(){
    Surface(modifier = Modifier
        .height(60.dp)
        .width(300.dp)
        .background(Color.Black), color=Color.Black, content = { Text("") })
}
@Composable
fun FronteCarta(carta: Carta?,utente:String) {
    val nrCarta = carta?.nrCarta.toString()
    val scadenza = carta?.let{ carta.dataScadenza.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} ?: ""
    val cvc = carta?.cvc
    val codUtente = carta?.codUtente
    var isFront by remember { mutableStateOf(true) }
    if (carta != null) {
        Log.d("Carta ","stato carta = $carta")
        Card (
            modifier = Modifier,
        ){
           Column(
                modifier = Modifier
                    .padding(0.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
           ) {
                if (!isFront) {
                    RigaMagnetica()
                    Spacer(modifier = Modifier.width(32.dp))
                    Text(text = "CVC: $cvc")
                    Text(text = stringResource(id = R.string.codice_utente) + codUtente)
                } else {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Simbolo sim",
                        modifier = Modifier.size(48.dp)
                    )
                    Text(text = nrCarta)
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text(text = utente)
                        Spacer(modifier = Modifier.width(32.dp))
                        Text(scadenza)
                    }
                }

                Button(onClick = { isFront = !isFront }) {
                    Text(text = "Back")
                }
            }
        }
    } else {
       Card (
           modifier = Modifier
               .padding(SmallPadding)
               .fillMaxWidth()
       ){
           Column (
               modifier = Modifier
                   .height(150.dp)
                   .fillMaxWidth(),
               verticalArrangement = Arrangement.Center
           ){
               Row (modifier = Modifier
                   .fillMaxWidth()
                   .padding(SmallPadding)
                   .align(Alignment.CenterHorizontally)
               ){
                   Text(text = stringResource(id = R.string.no_carte))
               }
           }
       }
    }
}