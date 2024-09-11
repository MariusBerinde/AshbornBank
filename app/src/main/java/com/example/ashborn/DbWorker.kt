package com.example.ashborn

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.OperationType
import com.example.ashborn.data.TransactionType
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.OperationRepository
import net.bytebuddy.jar.asm.TypeReference
import java.time.LocalDateTime
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * @brief: servizio che conclude le operazioni in sospeso
 * @param context contesto dell'applicazione
 * @param workerParameters parametri per esecuzione servizio
 */
class DbWorker(
    context: Context,
    workerParameters: WorkerParameters,
): CoroutineWorker(
    context,
    workerParameters,
){
    val context = context
    val ashbornDao: AshbornDao = AshbornDb.getDatabase(context).ashbornDao()
    val operationRepository = OperationRepository(ashbornDao)
    val className = DbWorker::class.java.name
    override suspend fun doWork(): Result {
        Log.d(className, "eseguo")
        dbWork()
        return Result.success()
    }

    suspend fun dbWork() {
        operationRepository.completePendingOperations()
    }
}

class PaymentWorker(
    context: Context,
    workerParameters: WorkerParameters,
): CoroutineWorker(
    context,
    workerParameters,
){
    val context = context
    val ashbornDao: AshbornDao = AshbornDb.getDatabase(context).ashbornDao()
    val operationRepository = OperationRepository(ashbornDao)
    val className = DbWorker::class.java.name
    override suspend fun doWork(): Result {
        Log.d(className, "eseguo")
        paymentWork()
        return Result.success()
    }

    private fun randomOperation(): Operation {
        val clientCodes = arrayListOf("")
        val bankAccounts = arrayListOf("")
        val descriptions = arrayListOf("")
        val ibans = arrayListOf("")
        val cardCodes = arrayListOf<Long?>(0)
        val recipents = arrayListOf("")
        val transactionTypes  = arrayListOf(TransactionType.DEPOSIT, TransactionType.WITHDRAWAL)
        val result = Operation(
            clientCode = clientCodes[(0..clientCodes.size).shuffled().first()],
            bankAccount = bankAccounts[(0..bankAccounts.size).shuffled().first()],
            amount = ((Random.nextDouble(1.0, 5000.0)*100).roundToInt().toDouble())/100,
            description = descriptions[(0..bankAccounts.size).shuffled().first()],
            dateV = LocalDateTime.now(),
            dateO = LocalDateTime.now(),
            transactionType = transactionTypes[(0..transactionTypes.size).shuffled().first()],
            iban = ibans[(0..ibans.size).shuffled().first()],
            cardCode = cardCodes[(0..cardCodes.size).shuffled().first()],
            operationStatus = OperationStatus.DONE,
            operationType = OperationType.CARD,
            recipient = recipents[(0..recipents.size).shuffled().first()],
        )
        Log.d(className, "operazione random creata: $result")
        return result
    }

    suspend fun paymentWork() {
        val operation = randomOperation()
        operationRepository.executeInstantTransaction(operation)
    }
}