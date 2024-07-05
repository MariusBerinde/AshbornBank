@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view.login

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ashborn.Validatore
import com.example.ashborn.viewModel.AshbornViewModel

enum class StatoErrore{ CONTENUTO, FORMATO, NESSUNO }

fun gestisciErrori(
    viewModel: AshbornViewModel
) {

    viewModel.setErroreNomeX(!Validatore().formatoNomeValido(viewModel.userName))
    viewModel.setErroreCognomeX(!Validatore().formatoCognomeValido(viewModel.cognome))
    viewModel.setErroreCodClienteX(!Validatore().formatoCodiceCliente(viewModel.codCliente))
    viewModel.setErroreDataNX(!Validatore().formatoDataNascitaValida(viewModel.dataNascita))

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

