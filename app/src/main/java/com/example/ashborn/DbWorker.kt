package com.example.ashborn

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.OperationRepository

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