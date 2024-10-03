package com.example.ashborn.view

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Carta
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
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
fun FronteCarta(carta: Carta?,utente:String) {
    val nrCarta = carta?.nrCarta.toString()
    val scadenza = carta?.let{ carta.dataScadenza.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} ?: ""
    val cvc = carta?.cvc
    val codUtente = carta?.codUtente
    var isFront by remember { mutableStateOf(true) }
    if (carta != null) {
        Log.d("Carta ","stato carta = $carta")
        Card (
            modifier = Modifier
                .width(300.dp)
                .height(235.dp)
                .padding(top = SmallPadding)
                .border(2.dp, Color.Black, RoundedCornerShape(15.dp)),
        ){
           Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
           ) {
                if (!isFront) {
                    Surface(
                        modifier = Modifier
                            .height(100.dp)
                            .width(300.dp)
                            .padding(top = LargePadding, bottom = SmallPadding)
                            .background(Color.Black),
                        color = Color.Black,
                        content = {},
                    )
                    Text(
                        modifier = Modifier.padding(bottom = SmallPadding),
                        text = "CVC: $cvc",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(id = R.string.codice_utente)+ ": " + codUtente,
                        modifier = Modifier.padding(bottom = SmallPadding),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,

                    )
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = SmallPadding),
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MediumPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Card(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(start = SmallPadding)
                                .border(
                                    2.dp,
                                    Color.Black,
                                    RoundedCornerShape(
                                        20,
                                        20,
                                        20,
                                        20
                                    )
                                ),
                            colors = CardColors(
                                Color(0xFFFFD700),
                                Color(0xFFFFD700),
                                Color(0xFFFFD700),
                                Color(0xFFFFD700)
                            ),
                        ) {

                        }
                        Text(
                            modifier = Modifier
                                .padding(horizontal = MediumPadding),
                            text = nrCarta,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(SmallPadding),
                    ) {
                        Text(
                            text = utente,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.width(50.dp))
                        Text(
                            text = scadenza,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            
               IconButton(onClick = { isFront = !isFront }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.arrow_u_left_top_bold),
                        contentDescription = "back"
                    )   
               }
            }
        }
    } else {
       Card (
           modifier = Modifier
               .padding(SmallPadding)
               .fillMaxWidth(),
       ){
           Column (
               modifier = Modifier
                   .height(150.dp)
                   .fillMaxWidth(),
               verticalArrangement = Arrangement.Center,
           ){
               Row (
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(SmallPadding)
                       .align(Alignment.CenterHorizontally),
               ){
                   Text(
                       text = stringResource(id = R.string.no_carte),
                       fontWeight = FontWeight.Bold,
                       textAlign = TextAlign.Center
                   )
               }
           }
       }
    }
}
