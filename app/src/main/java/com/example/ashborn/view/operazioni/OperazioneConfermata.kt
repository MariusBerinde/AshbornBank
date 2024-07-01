package com.example.ashborn.view.operazioni

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun OperazioneConfermata(navController : NavHostController, viewModel : OperationViewModel) {
    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(id = R.string.bonifico), fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.padding(top = 150.dp))
        Row () {
            Text(text = stringResource(id = R.string.conferma_operazione), fontSize = 20.sp)
        }
        Row () {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.emoticon),
                modifier = Modifier.size(256.dp),
                tint = Color.Yellow,
                contentDescription = "smile")
        }
        Row () {
            Button(onClick = { navController.navigate("bonifico") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.altro_bonifico))
            }
        }
        Row () {
            Button(onClick = { navController.navigate("conti") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.chiudi))
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewO() {
    val viewModel = AshbornViewModel()
    val viewModelOp = OperationViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Operazioni(
                navController = navController,
                viewModel =viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )
            //Bonifico(navController = navController, viewModelOp = viewModelOp)
            //BonificoConfermato(navController = navController, viewModel = viewModelOp)
            //RiepilogoBonifico(navController = navController, viewModel = viewModelOp)
        }
    }
}
*/