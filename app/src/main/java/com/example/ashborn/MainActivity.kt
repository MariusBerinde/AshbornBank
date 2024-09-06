package com.example.ashborn

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
    val workRequest = PeriodicWorkRequestBuilder<DbWorker>(
        20, //min 15 minuti
        TimeUnit.MINUTES
    ).build()
    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            "db_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
}

