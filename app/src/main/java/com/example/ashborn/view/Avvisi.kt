package com.example.ashborn.view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.StatoAvviso
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AvvisiViewModel
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Avvisi(
    navController: NavHostController,
    viewModel: AvvisiViewModel,
) {
    var avvisi: ArrayList<Avviso> = viewModel.listaAvvisi
    val nameFun = object {}.javaClass.enclosingMethod?.name
    BackHandler(enabled = true) {
        navController.navigate("altro")
    }
    Column() {
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = stringResource(id = R.string.avvisi),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.padding(LargePadding))
        }

        Spacer(modifier = Modifier.padding(MediumPadding))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(SmallPadding)
                    .border(
                        1.dp,
                        Color.Black,
                        shape = RoundedCornerShape(9.dp)
                    )
                    .fillMaxWidth()
                    .height(650.dp)
            ) {
                if(avvisi.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .padding(SmallPadding)
                                .fillMaxWidth()
                                .padding(SmallPadding),
                        ) {
                            Card(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(SmallPadding),
                                    text = stringResource(id = R.string.no_avvisi),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    }
                }

                for (i in avvisi) {
                    item {
                        Row(modifier = Modifier
                            .padding(SmallPadding)
                            .clickable {
                                Log.d(nameFun, "operazione inviata: ${i}")
                                val json = Json { prettyPrint = true }
                                val data = json.encodeToString(Avviso.serializer(), i)
                                viewModel.aggiornaStatoAvviso(i.id, StatoAvviso.LETTO)
                                i.stato = StatoAvviso.LETTO
                                navController.navigate("dettagli-avviso/$data")
                            }
                        ) {
                            Card(modifier = Modifier
                                .padding(SmallPadding)
                                .fillMaxWidth(),
                            ) {
                                val fontWeight =  if(i.stato == StatoAvviso.DA_LEGGERE) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MediumPadding)
                                ){
                                    Text(
                                        text = i.titolo,
                                        fontWeight = fontWeight
                                    )
                                    Spacer(modifier = Modifier.padding(SmallPadding))
                                    Text(
                                        text = i.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                        fontWeight = fontWeight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DettagliAvviso(navController: NavHostController, avviso:Avviso) {
    Column(modifier = Modifier.padding(SmallPadding)) {
        Row (modifier = Modifier.fillMaxWidth()){
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.padding(LargePadding))
            Text(
                text = stringResource(id = R.string.dettagli_avviso),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text = stringResource(id = R.string.titolo))
            Spacer(modifier = Modifier.padding(LargePadding))
            Text(text = avviso.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = avviso.titolo)
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.descrizione))
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row (
            modifier = Modifier.padding(SmallPadding)
        ){
            Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ) {
                Text(text = avviso.contenuto)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DettagliAvvisoPreview() {
    AshbornTheme {
        DettagliAvviso(navController = rememberNavController(), avviso = Avviso(0,"titolo1","fgthjyhmgvnbcxdsfdgrhtjgvnbcxzsafgdhfnvc xbSAfdgsfhfncvbxvzCsfadgshfdgncvxbczCSfadgshfgnvccxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", LocalDateTime.now(),""))
    }
}