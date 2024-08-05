package com.example.ashborn.view.operazioni

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.view.CustomDatePicker
import com.example.ashborn.view.DateUseCase
import com.example.ashborn.viewModel.OperationViewModel
import java.time.LocalDate

@Composable
fun Mav(
    navController: NavHostController,
    viewModel: OperationViewModel,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = stringResource(id = R.string.mav),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.padding(LargePadding))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.ordinante)) },
                value = viewModel.ordinanteMav,
                onValueChange = { viewModel.setOrdinanteMavX(it) }
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.codice_mav)) },
                value = viewModel.codiceMav,
                onValueChange = { viewModel.setCodiceMavX(it) }
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.importo)) },
                value = viewModel.importoMav.toString(),
                onValueChange = { viewModel.setImportoMavX(it) }
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.descrizione)) },
                value = viewModel.descrizioneMav,
                onValueChange = { viewModel.setDescrizioneMavX(it) },
            )
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            /*OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.dataAccredito)) },
                value = viewModel.dataAccreditoMav,
                onValueChange = { viewModel.setDataAccreditoMavX(it) },
                //trailingIcon =
            )*/
           // CustomDatePicker(useCase = DateUseCase.MAV, yearRange = LocalDate.now().year..LocalDate.now().plusYears(2).year)
        }
        Spacer(modifier = Modifier.padding(LargePadding))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(id = R.string.continua))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MavPreview() {
    AshbornTheme {
        val navController = rememberNavController()
        Mav(navController = navController,
            OperationViewModel(Application())
        )
    }
}
