package com.example.ashborn.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.ui.theme.MediumPadding
import kotlinx.serialization.json.Json

@Composable
fun IstruzioniDisconiscimento(
    navController: NavHostController,
){
   Column(
       modifier = Modifier.padding(MediumPadding),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center,
   ) {
       Text(
           text = stringResource(id = R.string.app_name),
           fontWeight = FontWeight.Bold,
           fontStyle = FontStyle.Italic
       )
       Spacer(modifier = Modifier.padding(8.dp) )
       Text("A causa di un problema tecnico non possiamo effettuare l'operazione via app.Prendi un appuntamento in una filiale portando i segueti documenti:")
       Spacer(modifier = Modifier.padding(8.dp) )
       Text(text = "- carta d'identità\n- appuntati i dettagli dell'operazione \n- copia della denuncia fatta alla polizia( o a un altro organo della polizia)")
       Spacer(modifier = Modifier.padding(8.dp) )
       Button(onClick = { navController.navigate("conti") }) {
           Text("Torna a conti")
       }

   }
}

@Composable
fun AnnullaOperazione(
    navController: NavHostController,
   operazion:Operation,
){
    Column(
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.padding(8.dp) )
        Text("Attenzione l'operazione è irreversibile")

        Spacer(modifier = Modifier.padding(8.dp) )
        Row {
            Button(onClick = { navController.navigate("conti") }) {
                Text("Torna a conti")
            }
            Spacer(modifier = Modifier.padding(8.dp) )
            Button(onClick = {
                operazion.operationStatus = OperationStatus.TO_DELETE
                val json = Json { prettyPrint = true }
                val data = json.encodeToString(Operation.serializer(), operazion)
                navController.navigate("pin/$data")


            }) {
                Text(text = "Continua")
            }
        }


    }
}

