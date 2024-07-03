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
import androidx.compose.ui.unit.dp
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.viewModel.AshbornViewModel


@Composable
fun BloccoPIN (
        viewModel: AshbornViewModel,
        content: @Composable BoxScope.() -> Unit
) {
    Log.i("BloccoPIN", "disegno BloccoPIN")
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        content(this)
        if (viewModel.wrongAttempts >= 5 && viewModel.tempoRimanente.value > 0) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .align(Alignment.Center),
                color = Color.Transparent

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(40.dp)
                ) {
                    Card {
                        Text(
                            text = stringResource(id = R.string.tempo_rimanente) + viewModel.testoTimer.value,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(LargePadding)
                        )
                    }
                }
            }
        }
    }
}