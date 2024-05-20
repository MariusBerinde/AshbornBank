@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view

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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.AppNavigazione
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Welcome(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Ashborn Bank \n Benvenuto " + viewModel.userName, 
             modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(SmallVerticalSpacing))
        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(SmallPadding),
            //TODO:aggiungere controllo per verifica primo login
               onClick = {
                   if (!viewModel.fistLogin) {
                       navController.navigate("login")
                   } else {
                       navController.navigate("primo-login")
                   }
               }

        ) {
           Text("Entra")
        }
    }
}

@Composable
fun AskPIN(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Inserisci il PIN")
        }
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = viewModel.pin,
                onValueChange = {viewModel.setPinX(it)}
            )
        }
        Spacer(modifier = Modifier.height(MediumPadding))
        //Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            

        for (i in (0 .. 2)) {
            Row(
               // modifier = Modifier.padding(SmallPadding)
            ) {
                for (j in (0 .. 2)) {

                    Spacer(modifier = Modifier.width(SmallVerticalSpacing))
               //     Spacer(modifier = Modifier.height(MediumPadding))
                    Button(
                        modifier = Modifier.size(70.dp,40.dp),
                        onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + (3*i+j).toString())}}) {
                        Text(text = (3*i+j+1).toString())
                    }
                }
            }


            Spacer(modifier = Modifier.padding(SmallPadding))

        }
        Row (){
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp,40.dp),
                onClick = {
                    if(!viewModel.checkPin()) {
                        viewModel.set_StartDest("principale")
                        navController.navigate("conti") {popUpTo("principale")}
                    } else {
                        viewModel.incrementWrongAttempts()
                    }
                }
            ) {
                Text(text = "OK")
            }

            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp,40.dp),
                onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + "0")}}) {
                Text(text = "0")
            }

            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
           Button(
               modifier = Modifier.size(70.dp,40.dp),
               onClick = {if(viewModel.pin.isNotEmpty()) {viewModel.setPinX(viewModel.pin.dropLast(1))} }) {
                //Text(text = "<xI")
               Icon( Icons.Filled.Clear, contentDescription = "icona cancellazione")
            }
        }

            
        //}
    }
}
@Composable
fun Registrazione(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    var nome_attuale by remember { mutableStateOf("") }
    var cognome_attuale by remember { mutableStateOf("") }
    var dataN by remember { mutableStateOf("") }
    var cod_attuale by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(MediumPadding)
            .padding(SmallPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Ashborn Bank", fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic) //TODO: da sistemare
        Spacer(modifier = Modifier.height(SmallPadding))
        Text(text = "Nome")
       TextField(
           value = nome_attuale,
           onValueChange = { nome_attuale = it },
           label = { Text("Inserisci nome") }
       )

        Spacer(modifier = Modifier.height(SmallPadding))

        Text(text = "Cognome")
        TextField(
            value = cognome_attuale,
            onValueChange = { cognome_attuale = it },
            label = { Text("Inserisci cognome") }
        )

        Spacer(modifier = Modifier.height(SmallPadding))
        Text(text = "Data di nascita")
        TextField(
            value = dataN,
            onValueChange = { dataN = it },
            label = { Text("Inserisci data di nascita") }
        )

        Spacer(modifier = Modifier.height(SmallPadding))
        Text(text = "Codice cliente")
        TextField(
            value = cod_attuale,
            onValueChange = { cod_attuale = it },
            label = { Text("Inserisci codice cliente") }
        )
        Spacer(modifier = Modifier.height(SmallPadding))
        Button(onClick = { /*TODO*/
            viewModel.setUserNameX(nome_attuale)
            viewModel.set_DataNascita(dataN)
            viewModel.set_Cognome(cognome_attuale)
            viewModel.set_CodCliente(cod_attuale)
        }) {
            Text(text = "Conferma")
        }
    }
}

@Preview(showBackground = true)
@Composable
 fun Preview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AskPIN(navController = navController, viewModel = viewModel)
        }
    }
}


