package com.example.ashborn.view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.PreviewAshbornViewModel

@Composable
fun Utente(
    viewModel: AshbornViewModel,
    viewModelp: PreviewAshbornViewModel?,

    navController: NavHostController
) {
//todo:Migliorare la grafica della pagina
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(LargePadding)
    ){
        val modifier = Modifier
            .fillMaxWidth()
            .padding(LargePadding)
            .align(Alignment.CenterHorizontally)
        Row(modifier = modifier) {
            Text(fontWeight = FontWeight.Bold, text = stringResource(id = R.string.nome))
            Text(text = viewModelp?.userName ?: viewModel.userName)
        }
        Row (modifier = modifier){
            Text(fontWeight = FontWeight.Bold, text = stringResource(id = R.string.cognome))
            Text(text = viewModelp?.cognome ?: viewModel.cognome)
        }
        Row(modifier = modifier) {
            Text(fontWeight = FontWeight.Bold, text = stringResource(id = R.string.data_nascita))
            Text(text = viewModelp?.dataNascita ?: viewModel.dataNascita)
        }
        Row(modifier = modifier){
            Text(fontWeight = FontWeight.Bold, text = stringResource(id = R.string.codice_cliente))
            Text(text = viewModelp?.codCliente ?: viewModel.codCliente)
        }
    }


}


class PrevievApp: Application()
@Preview(showBackground = true)
@Composable
fun AshbornPreview() {
    AshbornTheme {

        val viewModel: PreviewAshbornViewModel = PreviewAshbornViewModel(PrevievApp())

        val navController = rememberNavController()
        //Utente(viewModel = AshbornViewModel(PrevievApp()), viewModelp = viewModel, navController = navController)
        CustomDatePicker()
    }
}

