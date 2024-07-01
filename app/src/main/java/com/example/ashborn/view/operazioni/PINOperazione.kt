package com.example.ashborn.view.operazioni

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun PINOperazione(navController: NavHostController, viewModel: OperationViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(id = R.string.pin))
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = viewModel.pin,
                onValueChange = { viewModel.setPinX(it) },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Spacer(modifier = Modifier.height(MediumPadding))
        for (i in (0..2)) {
            Row(
                // modifier = Modifier.padding(SmallPadding)
            ) {
                for (j in (0..2)) {

                    Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                    //     Spacer(modifier = Modifier.height(MediumPadding))
                    Button(
                        modifier = Modifier.size(70.dp, 40.dp),
                        onClick = {
                            if (viewModel.pin.length <= 8) {
                                viewModel.setPinX(viewModel.pin + (3 * i + j).toString())
                            }
                        }
                    ) {
                        Text(text = (3 * i + j + 1).toString())
                    }
                }
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
        Row() {
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    if (!viewModel.checkPin()) {
                        viewModel.set_StartDest("operazioni")
                        navController.navigate("operazioneConfermata") { popUpTo("operazioneConfermata") }
                    } else {
                        viewModel.incrementWrongAttempts()
                        if (viewModel.wrongAttempts >= 3) {
                            viewModel.bloccaUtente()
                        }
                    }
                }
            ) {
                Text(text = "OK")
            }
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    if (viewModel.pin.length <= 8) {
                        viewModel.setPinX(viewModel.pin + "0")
                    }
                }) {
                Text(text = "0")
            }
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp, 40.dp),
                onClick = {
                    if (viewModel.pin.isNotEmpty()) {
                        viewModel.setPinX(viewModel.pin.dropLast(1))
                    }
                }) {
                //Text(text = "<xI")
                Icon(Icons.Filled.Clear, contentDescription = "icona cancellazione")
            }
        }
    }
}