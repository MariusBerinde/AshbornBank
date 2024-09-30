package com.example.ashborn.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.ContiViewModel

@Composable
fun Conti(
    navController: NavHostController,
    viewModel: ContiViewModel
) {
    val saldoNascosto = remember {mutableStateOf(true) }
    val larghezzaSchermo = LocalConfiguration.current.screenWidthDp

    if(viewModel.contoMostrato != null){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(MediumPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    modifier = Modifier.padding(top = 70.dp),
                    onClick = {viewModel.contoPrecedente()}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "precedente"
                    )
                }
                Card(
                    modifier = Modifier
                        .width(290.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(MediumPadding)
                        ) {
                            Text(
                                text = stringResource(id = R.string.conto) +
                                        ": " + viewModel.contoMostrato?.codConto,
                                modifier = Modifier,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(MediumPadding),
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                            ) {
                                Row (){
                                    Text(
                                        text = stringResource(id = R.string.saldo) + ": ",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = "%.2f".format(viewModel.contoMostrato?.saldo),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if(saldoNascosto.value) Color.Transparent else Color.Black
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.padding(start = 50.dp))
                            Column {
                                IconButton(
                                    onClick = {saldoNascosto.value = !saldoNascosto.value},
                                    modifier = Modifier
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            if(saldoNascosto.value) R.drawable.eye
                                            else R.drawable.eye_off
                                        ),
                                        contentDescription = "mostra saldo"
                                    )
                                }
                            }

                        }
                        Row(
                            modifier = Modifier
                                .padding(MediumPadding),
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "IBAN: " + viewModel.contoMostrato?.iban,
                                    fontSize = 12.sp,
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Spacer(modifier = Modifier.padding(start = 10.dp))
                            Column {
                                Share(
                                    text = viewModel.contoMostrato?.iban ?: "",
                                    context = LocalContext.current
                                )
                            }
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.padding(top = 70.dp),
                    onClick = {viewModel.contoSuccessivo()}
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "successivo"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
            Row (
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)

            ){
                Column(modifier = Modifier.padding(SmallPadding)) {
                    ListaOperazioniFatteConti(navController, viewModel)
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
                    Text(text = stringResource(id = R.string.no_conti))
                }
            }
        }
    }
}

@Composable
fun Share(text: String, context: Context) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    IconButton(
        onClick = {
            startActivity(context, shareIntent, null)
        },
        modifier = Modifier
    ) {
        Icon(
            Icons.Filled.Share,
            contentDescription = "share IBAN",
            modifier = Modifier.padding(SmallPadding)
        )
    }
}