package com.example.ashborn.view.login

import com.example.ashborn.viewModel.RegistrazioneViewModel
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