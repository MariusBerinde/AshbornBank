package com.example.ashborn.view

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.PreviewAshbornViewModel

@Composable
fun Utente(
    viewModel: AshbornViewModel,
    viewModelp: PreviewAshbornViewModel?,

    navController: NavHostController
) {
//todo:Migliorare la grafica della pagina
    Column(){
        Row(modifier = Modifier.fillMaxWidth()) {

            Text(text = "nome:" + " "+if (viewModelp==null) {viewModel.userName} else {viewModelp.userName})
        }
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text = "\ncognome:" + " "+if (viewModelp==null) {viewModel.cognome} else {viewModelp.cognome})
        }
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "\ndata di nascita:" + " "+if (viewModelp==null) {viewModel.dataNascita} else {viewModelp.dataNascita})
        }
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = "\ncodice cliente" + " "+if (viewModelp==null) {viewModel.codCliente} else {viewModelp.codCliente})
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
        Utente(viewModel = AshbornViewModel(PrevievApp()), viewModelp = viewModel, navController = navController)
    }
}

