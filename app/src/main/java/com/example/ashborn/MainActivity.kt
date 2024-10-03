package com.example.ashborn

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.ashborn.ui.theme.AshbornTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit




class MainActivity : ComponentActivity() {
    private var inactivityJob: Job? = null
    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setupPeriodicWork(applicationContext)

        setContent {
            AshbornTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    navController = rememberNavController()
                    AppNavigazione2(startDest = "init",navController)
                }
            }
        }
        setupTouchListener()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener(){
        val rootView : View = findViewById(android.R.id.content)
        rootView.setOnTouchListener{
            _, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                resetInactivityTimeout()
            }
            false
        }


    }

    private fun resetInactivityTimeout() {
        inactivityJob?.cancel()
        inactivityJob = CoroutineScope(Dispatchers.Main.immediate).launch {
            delay(300000L)
            navController.navigate("welcome"){
                popUpTo(navController.graph.startDestinationId){ inclusive = true }
            }

        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetInactivityTimeout()
    }

    override fun onDestroy() {
        super.onDestroy()
        inactivityJob?.cancel()
    }

}

fun setupPeriodicWork(context: Context){
    val workRequest = PeriodicWorkRequestBuilder<DbWorker>(
        20, //min 15 minuti
        TimeUnit.MINUTES
    ).build()
    val paymentWorkRequest = PeriodicWorkRequestBuilder<PaymentWorker>(
        20, //min 15 minuti
        TimeUnit.MINUTES
    ).build()
    WorkManager
        .getInstance(context)
        .enqueueUniquePeriodicWork(
            "db_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    WorkManager
        .getInstance(context)
        .enqueueUniquePeriodicWork(
            "payment_work",
            ExistingPeriodicWorkPolicy.KEEP,
            paymentWorkRequest
        )
}



