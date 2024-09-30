package com.example.ashborn.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding

@Composable
fun ParlaConNoi(navController: NavHostController) {

    val dest =  integerResource(R.integer.Conti)
    BackHandler(enabled = true) { navController.navigate("conti?index=$dest") }
    Column(
        modifier = Modifier
            .padding(MediumPadding)
           // .height(600.dp),
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val fontSize = 24.sp
        val modifier = Modifier.padding(bottom = 8.dp)
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.assist),
            fontSize = fontSize,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = modifier,
            text = "800 xxx xxxx ",
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.assist1),
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = "+39 011 xxx xxxx",
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.assist2),
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = "07.00 - 00.00 ",
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
        )
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.assist3),
            fontSize = fontSize,
        )
        Text(
            text = "09.00 - 19.00 ",
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
        )
    }
}
