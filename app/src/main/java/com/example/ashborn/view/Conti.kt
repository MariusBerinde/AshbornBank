package com.example.ashborn.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel
import java.time.LocalDateTime
import java.util.Date

@Composable
fun Conti(viewModel: AshbornViewModel) {
    Text(text = "1")
    Column (modifier = Modifier){
        Row (modifier = Modifier){
            Box(modifier = Modifier.padding(MediumPadding)) {
                Card (modifier = Modifier.padding(MediumPadding)) {
                    Row (modifier = Modifier.padding(MediumPadding)){
                        Text(text = "Conto: " + viewModel.codConto, modifier = Modifier)
                    }
                    Row (modifier = Modifier.padding(MediumPadding)) {
                        Column (modifier = Modifier.align(Alignment.CenterVertically)){
                            Text(text = "Saldo: " + viewModel.saldo, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.padding(start= 160.dp))
                        Column {
                            IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                                Icon(Icons.Filled.Info, contentDescription = "mostra saldo")
                            }
                        }
                    }
                    Row (modifier = Modifier.padding(MediumPadding)) {
                        Column (modifier = Modifier.align(Alignment.CenterVertically)){
                            Text(text = "IBAN: " + viewModel.IBAN, fontSize = 12.sp, modifier = Modifier)
                        }
                        Spacer(modifier = Modifier.padding(start= 20.dp))
                        Column {
                            IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                                Icon(Icons.Filled.Share, contentDescription = "share IBAN", modifier = Modifier.padding(SmallPadding))
                            }
                        }
                    }

                }
            }
        }
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row {
            Column (modifier = Modifier.padding(SmallPadding)){
                val voices = arrayListOf(LocalDateTime.now(), "bolletta", 167.0, "€")
                for (i in voices) {
                    Row (modifier = Modifier.padding(SmallPadding)){
                        Text(text = "")
                    }
                }
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ContiPreview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Conti(viewModel = viewModel)
        }
    }
}


