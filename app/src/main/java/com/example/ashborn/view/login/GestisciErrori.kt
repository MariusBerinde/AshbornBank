@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view.login

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ashborn.Validatore
import com.example.ashborn.viewModel.AshbornViewModel

fun gestisciErrori(
    nome:String,
    cognome: String,
    codCliente:String,
    dataN:String,
    viewModel: AshbornViewModel
) {
    viewModel.setErroreNomeX(!Validatore().formatoNomeValido(nome))
    viewModel.setErroreCognomeX(!Validatore().formatoCognomeValido(cognome))
    viewModel.setErroreCodClienteX(!Validatore().formatoCodiceCliente(codCliente))
    viewModel.setErroreDataNX(!Validatore().formatoDataNascitaValida(dataN))
}
/*@Preview(showBackground = true)
@Composable
 fun Preview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            //Registrazione(navController, viewModel)
            /*AskPIN(
                navController = navController,
                viewModel = viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )*/
            Welcome(
                navController = navController,
                viewModel = viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )
        }
    }
}
*/

