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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun RigaMagnetica(){
    Surface(modifier = Modifier
        .height(60.dp)
        .fillMaxWidth()
        .background(Color.Black), color=Color.Black, content = { Text("") })
}
@Composable
fun FronteCarta() {
    val nrCarta="1234 5678 9101 1121"
    val utente="Mariolone Bubbarello"
    val scadenza="06/06/06"
    val cvc="avadv"
    val codUtente="21435444325555324"
    var isFront by remember { mutableStateOf(true) }
  Card {
      Column(
          modifier =  Modifier.padding(0.dp,16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          if(!isFront){
              RigaMagnetica()
              Spacer(modifier = Modifier.width(32.dp))
              Text(text = "CVC: $cvc")
              Text(text = stringResource(id = R.string.codice_utente)+ codUtente)
          }else{
              Text(text = stringResource(id = R.string.app_name), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.width(32.dp))
              Icon(Icons.Filled.Star, contentDescription ="Simbolo sim", modifier = Modifier.size(48.dp))
              Text(text = nrCarta)
              Row (modifier = Modifier.padding(16.dp))
              {

                  Text(text = utente)
                  Spacer(modifier = Modifier.width(32.dp))
                  Text(scadenza)
              }
          }

          Button(onClick = {
              isFront= !isFront
          Log.i("Fronte carta","premo pulsante $isFront")
          }) {
              Text(text = "Back")
          }
      }
  }
}

@Composable
fun Carte(
    navController: NavHostController,
    viewModel: AshbornViewModel
) {
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        FronteCarta()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Column (modifier = Modifier.padding(SmallPadding)){
                ListaOperazioniFatte(navController, viewModel)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewC() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Carte (
                navController = navController,
                viewModel =viewModel
            )
        }
    }
}*/