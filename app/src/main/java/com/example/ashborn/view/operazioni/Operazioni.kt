package com.example.ashborn.view.operazioni

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ashborn.R
import com.example.ashborn.ui.theme.SmallPadding

@Composable
fun Operazioni(
    navController: NavHostController,
) {

        Column(
            modifier = Modifier
                //.fillMaxSize()
                .padding(16.dp)
                .height(800.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Custom bottom Bar
            val icons: ArrayList<ImageVector> = arrayListOf(
                ImageVector.vectorResource(R.drawable.bank),
                ImageVector.vectorResource(R.drawable.credit_card_outline),
                ImageVector.vectorResource(R.drawable.currency_eur),
                ImageVector.vectorResource(R.drawable.chat_outline),
                ImageVector.vectorResource(R.drawable.dots_horizontal)
            )
            val index = 0

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth().padding(vertical = SmallPadding),
            ) {

                Card(
                    onClick = {
                        navController.navigate("bonifico")

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(SmallPadding)
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Icon(icons[index], contentDescription = null)
                        Spacer(modifier = Modifier.padding(SmallPadding))
                        Text(
                            text = stringResource(id = R.string.bonifico),
                            fontWeight = FontWeight.Bold,
                        )
                    }


                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = SmallPadding),
            ) {

                Card(
                    onClick = { navController.navigate("mav") },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(SmallPadding)
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Icon(icons[index], contentDescription = null)
                        Spacer(modifier = Modifier.padding(SmallPadding))
                        Text(
                            text = stringResource(id = R.string.mav),
                            fontWeight = FontWeight.Bold,
                        )
                    }


                }
            }
        }

}

