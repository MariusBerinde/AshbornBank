package com.example.ashborn.view.operazioni

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun RiepilogoBonifico(navController : NavHostController, viewModel : OperationViewModel){
    Log.i("RiepilogoBonifico", "Sono entrato")
    val modifier = Modifier.padding(LargePadding)
    Column(modifier = Modifier.padding(LargePadding)) {
        Text(text = stringResource(id = R.string.riepilogo_bonifico), fontSize = 40.sp)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.ordinante), fontWeight = FontWeight.Bold)
        Text(text = viewModel.codConto)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.beneficiario), fontWeight = FontWeight.Bold)
        Text(text = viewModel.beneficiario)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.iban), fontWeight = FontWeight.Bold)
        Text(text = viewModel.iban)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.importo), fontWeight = FontWeight.Bold)
        Text(text = viewModel.importo)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.causale), fontWeight = FontWeight.Bold)
        Text(text = viewModel.causale)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.dataAccredito), fontWeight = FontWeight.Bold)
        Text(text = viewModel.dataAccredito)
        Spacer(modifier = modifier)
        Button(
            onClick = { navController.navigate("pin") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}