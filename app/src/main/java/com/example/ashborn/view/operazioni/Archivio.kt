package com.example.ashborn.view.operazioni

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding

@Composable
fun Archivio(
    navController: NavHostController,
    //viewModel: AshbornViewModel
) {
    Text(text = stringResource(id = R.string.archivio))
    Spacer(modifier = Modifier.padding(LargePadding))
}