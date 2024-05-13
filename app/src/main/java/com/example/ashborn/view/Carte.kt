package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.AshbornViewModel
@Composable
fun RigaMagnetica(modifier: Modifier = Modifier){
    Card(
        modifier = Modifier
            .size(200.dp, 50.dp)
            .background(Color.White)
    ) {

        Canvas(modifier = Modifier.size(200.dp,50.dp)) {
            val stripeWidth = size.width
            val stripeHeight = size.height
            val stripeStartX = 0f
            val stripeEndX = size.width

           drawPath(
               path = Path().apply {
                   moveTo(stripeStartX, stripeHeight / 2)
                   lineTo(stripeEndX,stripeHeight /2)
               },
               color = Color.Black,
               style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Square)
           )
        }
    }
}
@Composable
fun FronteCarta() {
    val nr_carta="1234 5678 9101 1121";
    val utente="Mariolone Bubbarello";
    val scadenza="06/06/06"
    val cvc="avadv"
    val cod_utente="21435444325555324"
    var isFront by remember { mutableStateOf(true) }
  Card {
      Column(
          modifier =  Modifier.padding(16.dp),
        //  verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          if(!isFront){
              RigaMagnetica(modifier = Modifier.padding(vertical = 16.dp))

                  Spacer(modifier = Modifier.width(32.dp))
                  Text(text = "CVC: $cvc")


              Text(text = "Codice utente: $cod_utente")
          }else{
              Text(text = "Ashborn bank", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.width(32.dp))
              Icon(Icons.Filled.Star, contentDescription ="Simbolo sim", modifier = Modifier.size(48.dp))
              Text(text = "$nr_carta")
              Row (modifier = Modifier.padding(16.dp))
              {

                  Text(text = "$utente")
                  Spacer(modifier = Modifier.width(32.dp))
                  Text("$scadenza")
              }
          }

          Button(onClick = {
              isFront= !isFront
          Log.i("Fronte cata","premo pulsante $isFront")
          }) {
              Text(text = "Back")
          }
      }
  }
}

@Composable
fun DettagliCarta(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        FronteCarta()
        Spacer(modifier = Modifier.height(16.dp))
       // DisegnaOperazioni()
    }
}


@Preview(showBackground = true) @Composable
fun preview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DettagliCarta (navController = navController, viewModel =viewModel )
        }
    }
}