package com.example.ashborn.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding

@Composable
fun WorkInProgress() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LargePadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.work_in_progress),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
