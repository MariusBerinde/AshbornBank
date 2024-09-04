package com.example.ashborn.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    if(viewModel.contoMostrato != null){
        Column(modifier = Modifier) {
            Row(modifier = Modifier) {
                Box(modifier = Modifier.padding(MediumPadding)) {
                    Card(modifier = Modifier.padding(MediumPadding)) {
                        Column {
                            Row(modifier = Modifier.padding(MediumPadding)) {
                                Text(
                                    text = stringResource(id = R.string.conto) + ": " + viewModel.contoMostrato?.codConto,
                                    modifier = Modifier
                                )
                            }
                            Row(modifier = Modifier.padding(MediumPadding)) {
                                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Text(
                                        text = stringResource(id = R.string.saldo) + ": " + viewModel.contoMostrato?.saldo,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.padding(start = 160.dp))
                                Column {
                                    IconButton(onClick = { }, modifier = Modifier) {
                                        Icon(Icons.Filled.Info, contentDescription = "mostra saldo")
                                    }
                                }
                            }
                            Row(modifier = Modifier.padding(MediumPadding)) {
                                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Text(
                                        text = "IBAN: " + viewModel.contoMostrato?.iban,
                                        fontSize = 12.sp,
                                        modifier = Modifier
                                    )
                                }
                                Spacer(modifier = Modifier.padding(start = 20.dp))
                                Column {
                                    Share(
                                        text = viewModel.contoMostrato?.iban ?: "",
                                        context = LocalContext.current
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
            Row {
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