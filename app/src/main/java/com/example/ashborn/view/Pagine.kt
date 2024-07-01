package com.example.ashborn.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.view.operazioni.Operazioni
import com.example.ashborn.viewModel.AshbornViewModel

@Composable
fun Pagine(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
) {
    val tabList: ArrayList<String> = arrayListOf("conti", "carte", "operazioni", "parla con noi", "altro")
    var selectedItem by remember { mutableIntStateOf(0) }
    val icons: ArrayList<ImageVector>  = arrayListOf(
        ImageVector.vectorResource(R.drawable.bank),
        ImageVector.vectorResource(R.drawable.credit_card_outline),
        ImageVector.vectorResource(R.drawable.currency_eur),
        ImageVector.vectorResource(R.drawable.chat_outline),
        ImageVector.vectorResource(R.drawable.dots_horizontal)
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                tabList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ErroreConnessione(connectionStatus = connectionStatus) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                when (selectedItem) {
                    0 -> Conti(
                        navController = navController,
                        viewModel = viewModel
                    )
                    1 -> Carte(
                        navController = navController,
                        viewModel = viewModel
                    )
                    2 -> Operazioni(
                        navController = navController,
                        viewModel = viewModel
                       // connectionStatus = connectionStatus
                    )
                    3 -> ParlaConNoi()
                    4 -> Altro(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun DettagliPreview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            //DettagliOperazione(0,navController,viewModel = viewModel)
            //Altro(navController, viewModel)
            Pagine(navController = navController, viewModel = viewModel, connectionStatus = ConnectivityObserver.Status.Lost)
        }
    }
}*/