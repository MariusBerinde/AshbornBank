package com.example.ashborn.view.operazioni

import com.example.ashborn.Validatore
import com.example.ashborn.viewModel.OperationViewModel

fun gestisciErrori(
    beneficiario: String,
    iban: String,
    importo: String,
    causale: String,
    dataAccredito: String,
    viewModelOp: OperationViewModel
) {
    viewModelOp.setErroreBeneficiarioX(!Validatore().formatoBeneficiarioValido(beneficiario))
    viewModelOp.setErroreIbanX(!Validatore().formatoIbanValido(iban))
    viewModelOp.setErroreImportoX(!Validatore().formatoImportoValido(importo))
    viewModelOp.setErroreCausaleX(!Validatore().formatoCausaleValida(causale))
    viewModelOp.setErroreDataAccreditoX(!Validatore().formatoDataAccreditoValida(dataAccredito))
}