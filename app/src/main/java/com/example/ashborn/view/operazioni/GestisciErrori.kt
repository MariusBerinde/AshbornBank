package com.example.ashborn.view.operazioni

import android.util.Log
import com.example.ashborn.Validatore
import com.example.ashborn.view.login.StatoErrore
import com.example.ashborn.viewModel.BonificoViewModel
import com.example.ashborn.viewModel.OperationViewModel
import java.time.format.DateTimeFormatter

fun gestisciErroriMav(
    beneficiario: String,
    iban: String,
    importo: String,
    causale: String,
    dataAccredito: String,
    viewModel: OperationViewModel//MavViewModel
) {
    viewModel.setErroreBeneficiarioX(!Validatore().formatoBeneficiarioValido(beneficiario))
    viewModel.setErroreIbanX(!Validatore().formatoIbanValido(iban))
    viewModel.setErroreImportoX(!Validatore().formatoImportoValido(importo))
    viewModel.setErroreCausaleX(!Validatore().formatoCausaleValida(causale))
    viewModel.setErroreDataAccreditoX(!Validatore().formatoDataAccreditoValida(dataAccredito))
}

fun gestisciErroriBonifico(
    beneficiario: String,
    iban: String,
    importo: String,
    causale: String,
    dataAccredito: String,
    viewModel: BonificoViewModel
) {
    val nameFun = object {}.javaClass.enclosingMethod?.name
    val statoErroreBeneficiario =  if(Validatore().formatoBeneficiarioValido(beneficiario)) StatoErrore.NESSUNO else StatoErrore.FORMATO
    val statoErroreIban = if(Validatore().formatoIbanValido(iban)) StatoErrore.NESSUNO else StatoErrore.FORMATO
    val statoErroreImporto = if(Validatore().formatoImportoValido(importo)) StatoErrore.NESSUNO else StatoErrore.FORMATO
    val statoErroreCausale = if(Validatore().formatoCausaleValida(causale)) StatoErrore.NESSUNO else StatoErrore.FORMATO
    val statoErroreData = if(Validatore().formatoDataAccreditoValida(dataAccredito)) StatoErrore.NESSUNO else StatoErrore.FORMATO

    viewModel.setErroreBeneficiarioX(statoErroreBeneficiario)
    viewModel.setErroreIbanX(statoErroreIban)
    viewModel.setErroreImportoX(statoErroreImporto)
    viewModel.setErroreCausaleX(statoErroreCausale)
    viewModel.setErroreDataAccreditoX(statoErroreData)
    Log.d(nameFun, "statoErroreIban: $statoErroreIban, statoErroreData: $statoErroreData")
}