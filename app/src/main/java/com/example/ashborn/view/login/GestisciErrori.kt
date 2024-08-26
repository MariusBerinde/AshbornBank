@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.ashborn.view.login

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ashborn.RegistrazioneViewModel
import com.example.ashborn.Validatore

enum class StatoErrore{ CONTENUTO, FORMATO, NESSUNO }

fun gestisciErrori(
    viewModel: RegistrazioneViewModel
) {
    val validatore = Validatore()
    viewModel.setErroreNomeX(
        if(validatore.formatoNomeValido(viewModel.userName)) StatoErrore.NESSUNO
        else StatoErrore.FORMATO
    )
    viewModel.setErroreCognomeX(
        if(validatore.formatoCognomeValido(viewModel.cognome)) StatoErrore.NESSUNO
        else StatoErrore.FORMATO
    )
    viewModel.setErroreCodClienteX(
        if(validatore.formatoCodiceCliente(viewModel.codCliente)) StatoErrore.NESSUNO
        else StatoErrore.FORMATO

    )
    viewModel.setErroreDataNascitaX(
        if(validatore.formatoDataNascitaValida(viewModel.dataNascita)) StatoErrore.NESSUNO
        else StatoErrore.FORMATO

    )

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

