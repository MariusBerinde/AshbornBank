package com.example.ashborn.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Conti(navController: NavHostController, viewModel: AshbornViewModel) {
    val tabList: ArrayList<String> = arrayListOf("conti", "carte", "operazioni", "parla con noi", "altro")
    var selectedItem by remember { mutableIntStateOf(0) }
    val icons: ArrayList<ImageVector> = arrayListOf(Icons.Filled.Home,Icons.Filled.Home,Icons.Filled.Home,Icons.Filled.Home, Icons.Filled.MoreVert)
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar () {
                tabList.forEachIndexed{ index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = { Icon(icons[index], contentDescription= item ) },
                        label = { Text(item) }
                    )
                }

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
            Row {
                Box(modifier = Modifier) {

                }
            }
            Row {

            }
        }

    }
}

