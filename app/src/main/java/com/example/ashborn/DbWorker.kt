package com.example.ashborn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * @brief: servizio che conclude le operazioni in sospeso
 * @param context contesto dell'applicazione
 * @param workerParameters parametri per esecuzione servizio
 */
class DbWorker(
    val context: Context,
    workerParameters: WorkerParameters,
): CoroutineWorker(
    context,
    workerParameters,
){
    val ashbornDao: AshbornDao = AshbornDb.getDatabase(context).ashbornDao()
    private val operationRepository = OperationRepository(ashbornDao)
    private val className = DbWorker::class.java.name

    override suspend fun doWork(): Result {
        Log.d(className, "eseguo")
        dbWork()
        return Result.success()
    }

    private suspend fun dbWork() {
        operationRepository.completePendingOperations()
    }
}

class PaymentWorker(
    val context: Context,
    workerParameters: WorkerParameters,
): CoroutineWorker(
    context,
    workerParameters,
){
    val ashbornDao: AshbornDao = AshbornDb.getDatabase(context).ashbornDao()
    private val operationRepository = OperationRepository(ashbornDao)
    private val className = PaymentWorker::class.java.name
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

    private suspend fun paymentWork() {
        Log.d(className, "paymentWork")
        val nameFun = object{}.javaClass.enclosingMethod?.name
        val operation = randomOperation()
        Log.d(nameFun, "operazione random creata: $operation")

        val notificationHandler = NotificationHandler(context)
        operationRepository.executePaymentTransaction(operation)
        if (
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(nameFun, "mando notifica")
            notificationHandler.showSimpleNotification(operation)
        }
    }
}

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationChannelID = "notification_channel_id"
    private val notificationChannel = NotificationChannel(notificationChannelID,"payment", NotificationManager.IMPORTANCE_HIGH)

    // SIMPLE NOTIFICATION
    fun showSimpleNotification(operation: Operation) {
        notificationManager.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(context.resources.getString(R.string.operazione_eseguita))
            .setContentText("""
                ${context.resources.getString(R.string.notifica)}${operation.amount}
                ${context.resources.getString(R.string.notifica2)}${operation.recipient}
                ${context.resources.getString(R.string.notifica3)}${operation.description}
                ${context.resources.getString(R.string.notifica4)}${operation.dateO.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
            """.trimIndent())
            .setSmallIcon(R.drawable.bank)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}