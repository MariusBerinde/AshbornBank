package com.example.ashborn.view

import ErroreGenerico
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
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
fun Pagine(navController: NavHostController, viewModel: AshbornViewModel) {
    val tabList: ArrayList<String> = arrayListOf("conti", "carte", "operazioni", "parla con noi", "altro")
    var selectedItem by remember { mutableIntStateOf(0) }
    val icons: ArrayList<ImageVector> = arrayListOf(Icons.Filled.Home,Icons.Filled.Home,Icons.Filled.Home,Icons.Filled.Home, Icons.Filled.MoreVert)
    val tabs =
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
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> Conti(viewModel)
                1 -> Carte(navController = navController, viewModel = viewModel)
                2 -> Operazioni()
                3 -> ErroreGenerico(navController = navController, viewModel =viewModel )
                4 -> Altro()
            }
        }

    }
}
/*

@Composable
fun Conti() {
    Text(text = "1")
}
*/


@Composable
fun Operazioni() {

    Text(text = "3")
}
@Composable
fun ParlaConNoi() {

    Text(text = "4")
}
@Composable
fun Altro() {

    Text(text = "5")
}
