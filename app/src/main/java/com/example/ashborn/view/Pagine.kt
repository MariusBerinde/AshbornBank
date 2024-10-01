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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.view.operazioni.Operazioni
import com.example.ashborn.viewModel.AltroViewModel
import com.example.ashborn.viewModel.CarteViewModel
import com.example.ashborn.viewModel.ContiViewModel

@Composable
fun Pagine(
    navController: NavHostController,
    viewModelConti: ContiViewModel,
    viewModelCarte: CarteViewModel,
    viewModelAltro: AltroViewModel,
    indice: Int = 0
) {
    val tabList: ArrayList<String> = arrayListOf(
        stringResource(R.string.conti),
        stringResource(R.string.carte),
        stringResource(R.string.operazioni),
        stringResource(R.string.parla),
        stringResource(R.string.altro),
    )
    var selectedItem by remember { mutableIntStateOf(indice) }
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
                        label = {
                            Text(
                                text = item,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
             //   .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (selectedItem) {
                0 -> {
                    Conti(
                        navController = navController,
                        viewModel = viewModelConti
                    )
                }
                1 -> {
                    Carte(
                        navController = navController,
                        viewModel = viewModelCarte
                    )
                }
                2 -> Operazioni(navController)
                3 -> ParlaConNoi(navController)
                4 -> {
                    Altro(
                        navController = navController,
                        viewModel = viewModelAltro
                    )
                }
            }
        }
    }
}

