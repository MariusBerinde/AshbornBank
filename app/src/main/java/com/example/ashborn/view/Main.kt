package com.example.ashborn.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Conti(navController: NavHostController, viewModel: AshbornViewModel) {
    val tabList: ArrayList<String> = arrayListOf("conti", "carte", "operazioni", "parla con noi", "altro")
    Scaffold (
        bottomBar = {
            NavigationBar () {
                for (i in tabList) {
                    Button(onClick = {navController.navigate(i)}) {
                        //TODO("add icon to button")
                        Text(text = i)
                    }
                }
                
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
        ) {

        }

    }
}

