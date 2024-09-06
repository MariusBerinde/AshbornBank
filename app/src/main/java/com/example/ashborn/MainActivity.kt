package com.example.ashborn

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.ui.theme.AshbornTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPeriodicWork(applicationContext)
        setContent {
            AshbornTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigazione2(startDest = "init")
                }
            }
        }
    }

}

fun setupPeriodicWork(context: Context){
    val workRequest = PeriodicWorkRequestBuilder<DbWorker>(1,TimeUnit.MINUTES).build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork("db_work",ExistingPeriodicWorkPolicy.KEEP,workRequest)
}

/**
 * @brief: servizio che conclude le operazioni in sospeso
 */
class DbWorker(context: Context,workerParameters: WorkerParameters):CoroutineWorker(context,workerParameters){
    val context = context
    suspend fun funzione_vuota(){
        Log.d("funzione vuota","faccio cossee")
      //  println("sono la funzione vuota")
    }
    override suspend fun doWork(): Result {

        val ashbornDao: AshbornDao = AshbornDb.getDatabase(context).ashbornDao()
        return Result.success()
    }
}