package com.example.ashborn.view.operazioni

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding

@Composable
fun OperazioneConfermata(navController : NavHostController) {
    val dest =  integerResource(R.integer.Operazioni)
    BackHandler(enabled = true) {}
    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(id = R.string.operazione_confermata), fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.padding(top = 150.dp))
        Row {
            Text(text = stringResource(id = R.string.conferma_operazione), fontSize = 20.sp)
        }
        Row {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.emoticon),
                modifier = Modifier.size(256.dp),
                tint = Color.Yellow,
                contentDescription = "smile")
        }
        Row {
            Button(
                onClick = {navController.navigate(dest)},
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.altra_operazione))
            }
        }
        Row {
            Button(onClick = { navController.navigate("conti") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.chiudi))
            }
        }
    }
}

@Composable
fun OperazioneRifiutata(navController: NavHostController) {
    BackHandler(enabled = true) {
        // Non fare nulla, in questo modo blocca l'azione "back"
        // Puoi anche gestire un'azione specifica qui
    }
    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(id = R.string.operazione_rifiutata), fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.padding(top = 150.dp))
        Row {
            Text(text = stringResource(id = R.string.rifiuto_operazione), fontSize = 20.sp)
        }
        Row {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.emoticon_sad),
                modifier = Modifier.size(256.dp),
                tint = Color.Yellow,
                contentDescription = "smile")
        }
        Row {
            Button(onClick = { navController.navigate("bonifico") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.altra_operazione))
            }
        }
        Row {
            Button(onClick = { navController.navigate("conti") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.chiudi))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOperazioneConfermata() {
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OperazioneRifiutata(navController = navController)
        }
    }
}
