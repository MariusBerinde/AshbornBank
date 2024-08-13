package com.example.ashborn.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.CardRepository
import com.example.ashborn.repository.ContoRepository
import com.example.ashborn.repository.OperationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DettagliOperazioneViewModel(application: Application): AndroidViewModel(application) {
    val ashbornDao = AshbornDb.getDatabase(application).ashbornDao()
    val operationRepository = OperationRepository(ashbornDao)
    val contoRepository = ContoRepository(ashbornDao)
    val cardRepository = CardRepository(ashbornDao)

    fun revocaOperazione(operation: Operation) {
        viewModelScope.launch {
            operationRepository.deleteOperation(operation)
            contoRepository.aggiornaSaldo(
                operation.clientCode,
                operation.bankAccount,
                operation.amount * if(operation.operationType == TransactionType.WITHDRAWAL) 1  else -1,
            )
        }
    }

    fun disconosciOperazione(operation: Operation) {
        viewModelScope.launch {
            operationRepository.deleteOperation(operation)
            if (operation.cardCode != null) {
                cardRepository.bloccaCarta(operation.clientCode, operation.cardCode)
            }
            delay(5000)
            contoRepository.aggiornaSaldo(
                operation.clientCode,
                operation.bankAccount,
                operation.amount * if(operation.operationType == TransactionType.WITHDRAWAL) 1  else -1,
            )
        }
    }

}

@Suppress("UNCHECKED_CAST")
class DettagliOperazioneViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DettagliOperazioneViewModel::class.java)) {
            return DettagliOperazioneViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}