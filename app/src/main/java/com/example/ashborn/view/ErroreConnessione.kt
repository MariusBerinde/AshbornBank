package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding

@Composable
fun ErroreConnessione(
    connectionStatus: ConnectivityObserver.Status,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Log.i("ErroreConnessione","stato connessione $connectionStatus")
        content(this)
        if (connectionStatus == ConnectivityObserver.Status.Lost ||
            connectionStatus == ConnectivityObserver.Status.Unavailable
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .align(Alignment.Center),
                color = Color.Transparent

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(LargePadding)
                ) {
                    Card {
                        Text(
                            text = stringResource(id = R.string.connessione_persa),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(LargePadding)
                        )
                    }
                }
            }
        }
    }
}