package com.example.ashborn.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.data.Avviso
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AvvisiViewModel
import java.time.format.DateTimeFormatter

@Composable
fun Avvisi(
    navController: NavHostController,
    viewModel: AvvisiViewModel,
) {
    var avvisi: ArrayList<Avviso> = viewModel.listaAvvisi
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.padding(LargePadding))
            Text(
                text = stringResource(id = R.string.avvisi),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.padding(MediumPadding))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(SmallPadding)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                if(avvisi.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .padding(SmallPadding)
                                .fillMaxWidth()
                                .padding(SmallPadding),
                        ) {
                            Card() {
                                Text(text = stringResource(id = R.string.no_avvisi))
                            }
                        }
                    }
                }
                val modifier = Modifier.padding(16.dp, 18.dp, 2.dp, 4.dp)

                for (i in avvisi) {
                    item {
                        Row(modifier = Modifier.padding(SmallPadding)) {
                            Card(modifier = Modifier.padding(SmallPadding).fillMaxWidth()) {
                                Text(
                                    text = i.titolo + " " +
                                            i.data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " +
                                            i.stato.toString(),
                                    modifier = modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}