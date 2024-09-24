package com.example.ashborn.view.operazioni

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun Operazioni(
    navController: NavHostController,
    viewModel: OperationViewModel
) {
    Box(
        modifier = Modifier,

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Custom Top Bar
            val icons: ArrayList<ImageVector> = arrayListOf(
                ImageVector.vectorResource(R.drawable.bank),
                ImageVector.vectorResource(R.drawable.credit_card_outline),
                ImageVector.vectorResource(R.drawable.currency_eur),
                ImageVector.vectorResource(R.drawable.chat_outline),
                ImageVector.vectorResource(R.drawable.dots_horizontal)
            )
            val index = 0

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Card(
                    onClick = {
                        navController.navigate("bonifico")

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(SmallPadding)
                    ) {

                        Icon(icons[index], contentDescription = "ll")
                        Spacer(modifier = Modifier.padding(SmallPadding))
                        Text(text = "Bonifico")
                    }


                }
            }

            Spacer(
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Card(
                    onClick = {

                        navController.navigate("mav")

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(SmallPadding)
                    ) {

                        Icon(icons[index], contentDescription = "ll")
                        Spacer(modifier = Modifier.padding(SmallPadding))
                        Text(text = "MAV/RAV")
                    }


                }
            }
        }
    }
}

