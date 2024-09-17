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
    val className = PaymentWorker::class.java.name
    override suspend fun doWork(): Result {
        Log.d(className, "eseguo payment")
        paymentWork()
        return Result.success()
    }

    private fun randomOperation(): Operation {
        val clientCodes = arrayListOf(
            "777777777",
            "666666666",
        )
        val bankAccounts = arrayListOf(
            "42",
            "43",
            "44",
        )
        val recipients = arrayListOf(
            "",
            "",
            ""
        )
        val ibans = arrayListOf(
            "GB82WEST12345698765432",
            "IT60X0542811101000000123456",
            "DE89370400440532013000",
        )
        val cardCodes = arrayListOf<Long?>(
            1111222233334444,
        )
        val descriptions = arrayListOf(
            "Spesa affitto Nazgul",
            "Rapimento villico innocente",
            "Pagamento maghi per torture prigionieri",
            "Affitto Nave Spaziale",
            "Pagamento lezioni di manipolazione dei protagonisti",
            "Affitto mutanti per distruzione di massa",
            "Tortura Genitori di Neville",
        )
        val indexSender = (0..< cardCodes.size).shuffled().first()
        var indexReceiver: Int
        do  {indexReceiver = (0..< ibans.size).shuffled().first()} while (indexReceiver == indexSender)
        val result = Operation(
            bankAccount = bankAccounts[indexSender],
            amount = ((Random.nextDouble(1.0, 5000.0)*100).roundToInt().toDouble())/100,
            description = descriptions[(0..bankAccounts.size).shuffled().first()],
            dateV = LocalDateTime.now(),
            dateO = LocalDateTime.now(),
            transactionType = TransactionType.WITHDRAWAL,
            cardCode = cardCodes[indexSender],
            clientCode = clientCodes[indexSender],
            iban = ibans[indexReceiver],
            operationStatus = OperationStatus.DONE,
            operationType = OperationType.CARD,
            recipient = recipients[indexReceiver],
        )
        Log.d(className, "operazione random creata: $result")
        return result
    }

    suspend fun paymentWork() {
        Log.d(className, "paymentWork")
        val nameFun = object{}.javaClass.enclosingMethod?.name
        val operation = randomOperation()
        Log.d(nameFun, "operazione random creata: $operation")
        operationRepository.executePaymentTransaction(operation)
    }
}