package com.example.ashborn.view

import ParlaConNoi
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.viewModel.AshbornViewModel
import androidx.compose.ui.res.stringResource


@Composable
fun Pagine(navController: NavHostController, viewModel: AshbornViewModel) {
    val tabList: ArrayList<String> = arrayListOf("conti", "carte", "operazioni", "parla con noi", "altro")
    var selectedItem by remember { mutableIntStateOf(0) }
    val icons: ArrayList<ImageVector>  = arrayListOf<ImageVector>(
        ImageVector.vectorResource(R.drawable.bank),
        ImageVector.vectorResource(R.drawable.credit_card_outline),
        ImageVector.vectorResource(R.drawable.currency_eur),
        ImageVector.vectorResource(R.drawable.chat_outline),
        ImageVector.vectorResource(R.drawable.dots_horizontal)
    )
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar () {
                tabList.forEachIndexed{ index, item ->
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
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> Conti(navController = navController, viewModel = viewModel)
                1 -> Carte(navController = navController, viewModel = viewModel)
                2 -> Operazioni(navController = navController, viewModel = viewModel)
                3 -> ParlaConNoi(navController = navController, viewModel =viewModel )
                4 -> Altro(navController, viewModel)
            }
        }

    }
}

@Composable
fun Operazioni(navController : NavHostController ,viewModel : AshbornViewModel) {
    OperazioniDisponibili(navController = navController, viewModel = viewModel)
}

@Composable
fun DettagliOperazione(index_operation: Long,navController: NavHostController, viewModel: AshbornViewModel) {
    var op=viewModel.arrayOperazioni.find { e->e.id==index_operation}
    Log.i("Dettagli operazione","Oggetto $index_operation")
    Log.i("Dettagli operazione","Oggetto $op")
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Custom Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton( onClick = { navController.popBackStack() } ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.dettagli_operazione),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Riquadro A: Centrato nella pagina
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Rettangolo per tipoOperazione
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                            .background(Color.LightGray) // Optional: background color
                    ) {
                        Text(text = "Tipo di Operazione: ${op!!.operationType}", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Ammontare: ${op!!.amount}", fontSize = 24.sp)
                }
            }
            // Riquadro B: Contiene Data operazione, descrizione e pulsanti share e delete
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.dataO) + op!!.dateO.toLocalDate().toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.descrizione) + op!!.description,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                           Icon(
                               imageVector = Icons.Default.Share,
                               contentDescription = "share",
                               tint = Color.Gray
                           )
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Red),
                            onClick = { /* Azione delete */ }) {
                            Text(
                                //TODO true se non ancora eseguita false altrimenti
                                if (true) {
                                    stringResource(id = R.string.revoca)
                                } else {
                                    stringResource(id = R.string.disconosci)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
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
            Altro(navController, viewModel)
        }
    }
}