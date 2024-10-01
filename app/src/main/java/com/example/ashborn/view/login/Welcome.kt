package com.example.ashborn.view.login

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.WelcomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun Welcome(
    viewModel: WelcomeViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val nameFun = object{}.javaClass.enclosingMethod?.name
    // Verifica se il permesso è già stato concesso
    val isNotificationPermissionGranted = remember {
        checkNotificationPermission(context)
    }

    // Ricorda se il permesso è stato richiesto in precedenza
    val hasRequestedPermission = viewModel.hasRequestedPermission

    // Questo launcher verrà utilizzato per richiedere il permesso
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )
    Log.d(nameFun, """
        hasRequestedPermission: $hasRequestedPermission
        isNotificationPermissionGranted: $isNotificationPermissionGranted
        """.trimIndent()
    )
    // Mostra il permesso solo se non è stato concesso e non è stato già richiesto
    if (!isNotificationPermissionGranted && !hasRequestedPermission) {
        viewModel.setHasRequestedPermissionX(true)
        // Mostra il dialog di richiesta permesso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LaunchedEffect(Unit) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(MediumPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.app_name) + "\n" +
                        stringResource(id = R.string.welcome) + "\n" +
                        viewModel.getUsername(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(MediumPadding),
                onClick = {
                    if (viewModel.getUsername().isBlank()) {
                        Log.d("Welcome","verso registrazione")
                        navController.navigate("primo-login")
                    } else {
                        Log.d("Welcome","verso pin")
                        navController.navigate("login")
                        Log.d("welcome", viewModel.getUsername())
                    }
                },
            ) {
                Text(stringResource(id = R.string.entra))
            }
        }
    }
}

// Funzione per verificare se il permesso è concesso
fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        // Per versioni Android precedenti, non è necessario richiedere permessi espliciti
        true
    }
}

// Salva lo stato del permesso di notifica (puoi usare DataStore per memorizzarlo in modo persistente)
