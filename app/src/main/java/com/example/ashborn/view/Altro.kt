package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Voice
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel
import androidx.compose.ui.res.stringResource
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding

@Composable
fun Altro(navController: NavHostController, viewModel: AshbornViewModel) {
    Column() {

        Row (modifier = Modifier.fillMaxWidth()){
            Card (modifier = Modifier
                .padding(MediumPadding)
                .fillMaxWidth()
                .padding(MediumPadding)) {
                Row {
                    Column (modifier = Modifier.padding(MediumPadding)) {
                        Box(modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Blue)
                            .padding(MediumPadding)
                        ) {
                            Text(
                                text = "PP",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(20.dp),
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column {
                        Text(stringResource(id = R.string.profilo))
                        Text(viewModel.userName)
                        Text(stringResource(id = R.string.app_name))

                    }
                    Column (modifier = Modifier
                        .padding(horizontal = SmallPadding, vertical = 50.dp)
                        .clickable { }){
                        Text("Dettagli", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Row {
            Column (modifier = Modifier.padding(SmallPadding)){
                val voci: ArrayList<Operation> = viewModel.arrayOperazioni
                ListaAzioni(navController,voci = voci)
            }
        }
    }
}


@Composable
fun ListaAzioni(navController: NavHostController, voci: ArrayList<Operation>,) {
    Column(modifier = Modifier.run {
        padding(SmallPadding)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
            .fillMaxWidth()
            .height(450.dp)
    }
    ) {
        val voci: ArrayList<Voice> = arrayListOf(
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.avvisi), "avvisi"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.archivio), "archivio"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.sicurezza), "sicurezza"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.impostazioni), "impostazioni"),
            Voice(Icons.Filled.PlayArrow, stringResource(id = R.string.logout), "logout")
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
                        Log.i("OperazioniAltro", "${i.name}")
                        navController.navigate(i.destination)
                    }
                ) {
                    Icon(i.icon, contentDescription = "icona")
                    Text(i.name)
                }

            }
        }
    }
}

@Composable
fun Avvisi(navController: NavHostController, viewModel: AshbornViewModel) {
    Text(text = stringResource(id = R.string.avvisi))

}

@Composable
fun Archivio(navController: NavHostController, viewModel: AshbornViewModel) {
    Text(text = stringResource(id = R.string.archivio))
    Spacer(modifier = Modifier.padding(LargePadding))

}
@Composable
fun Sicurezza(navController: NavHostController, viewModel: AshbornViewModel) {
    Text(text = stringResource(id = R.string.sicurezza))

}
@Composable
fun Impostazioni(navController: NavHostController, viewModel: AshbornViewModel) {
    Text(text = stringResource(id = R.string.impostazioni))
}
@Composable
fun Logout(navController: NavHostController, viewModel: AshbornViewModel) {
    Text(text = stringResource(id = R.string.logout))

}

@Preview(showBackground = true)
@Composable
fun OpPreview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize() ,
            color = MaterialTheme.colorScheme.background
        ) {
            Altro(navController, viewModel)
        }
    }
}