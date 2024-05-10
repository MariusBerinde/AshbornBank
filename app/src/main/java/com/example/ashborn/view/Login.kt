@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
        Spacer(modifier = Modifier.height(SmallVerticalSpacing))
        //Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            

        for (i in (0 .. 2)) {
            Row() {
                for (j in (0 .. 2)) {
                    Button(onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + (3*i+j).toString())}}) {
                        Text(text = (3*i+j+1).toString())
                    }
                }
            }


            if (i % 3 == 0) {
                Spacer(modifier = Modifier.padding(SmallPadding))
            }
        }
        Row (){
            Button(
                onClick = {
                    if(!viewModel.checkPin()) {
                        viewModel.set_StartDest("principale")
                        navController.navigate("utente") {popUpTo("principale")}
                    } else {
                        viewModel.incrementWrongAttempts()
                    }
                }
            ) {
                Text(text = "OK")
            }
            Button(onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + "0")}}) {
                Text(text = "0")
            }
            Button(onClick = {if(viewModel.pin.isNotEmpty()) {viewModel.setPinX(viewModel.pin.dropLast(1))} }) {
                Text(text = "<xI")
            }
        }

            
        //}
    }
}
@Composable
fun Registrazione(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .padding(MediumPadding)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Ashborn Bank", fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic) //TODO: da sistemare
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Nome")
        TextField(value = "", onValueChange ={viewModel.setUserNameX(it)} )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Cognome")
        TextField(value = "", onValueChange ={viewModel.set_Cognome(it)} )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Data di nascita")
        TextField(value = "", onValueChange ={viewModel.set_DataNascita(it)} )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Codice cliente")
        TextField(value = "", onValueChange ={} )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/

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
            Welcome(navController = navController, viewModel = viewModel)
        }
    }
}


