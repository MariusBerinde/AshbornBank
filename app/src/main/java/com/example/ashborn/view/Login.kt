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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
               onClick = {navController.navigate("login")}

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
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(modifier = Modifier
                .padding(SmallPadding),
                onClick = { if(viewModel.checkPin()) {navController.navigate("Login"){navController.popBackStack()}} else {} }
            ) {

                for (i in (1 .. 9)) {
                    Button(onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + i.toString())}}) {
                        Text(text = i.toString())
                    }
                }
                Button(
                    onClick = {
                        if(viewModel.checkPin()) {
                            navController.navigate("home") {navController.popBackStack()}
                        } else {
                            viewModel.setPinX("")
                            viewModel.incrementWrongAttempts()
                            //TODO("add timer if more than 3 consecutive wrong attempts and show error")
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


