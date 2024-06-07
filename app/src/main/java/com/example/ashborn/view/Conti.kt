package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.data.Operation
import com.example.ashborn.data.TransactionType
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.viewModel.AshbornViewModel
import androidx.compose.material3.Card as Card
import androidx.compose.ui.res.stringResource
import com.example.ashborn.R

@Composable
fun Conti(navController: NavHostController, viewModel: AshbornViewModel) {
    Column (modifier = Modifier){
        Row (modifier = Modifier){
            Box(modifier = Modifier.padding(MediumPadding)) {
                Card (modifier = Modifier.padding(MediumPadding)) {
                    Row (modifier = Modifier.padding(MediumPadding)){
                        Text(text = stringResource(id = R.string.conto)+": " + viewModel.codConto, modifier = Modifier)
                    }
                    Row (modifier = Modifier.padding(MediumPadding)) {
                        Column (modifier = Modifier.align(Alignment.CenterVertically)){
                            Text(text = stringResource(id = R.string.saldo)+": " + viewModel.saldo, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.padding(start= 160.dp))
                        Column {
                            IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                                Icon(Icons.Filled.Info, contentDescription = "mostra saldo")
                            }
                        }
                    }
                    Row (modifier = Modifier.padding(MediumPadding)) {
                        Column (modifier = Modifier.align(Alignment.CenterVertically)){
                            Text(text = "IBAN: " + viewModel.IBAN, fontSize = 12.sp, modifier = Modifier)
                        }
                        Spacer(modifier = Modifier.padding(start= 20.dp))
                        Column {
                            IconButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                                Icon(Icons.Filled.Share, contentDescription = "share IBAN", modifier = Modifier.padding(SmallPadding))
                            }
                        }
                    }

                }
            }
        }
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row {
            Column (modifier = Modifier.padding(SmallPadding)){
                val voci: ArrayList<Operation> = viewModel.arrayOperazioni
                ListaOperazioniFatte(navController,voci = voci)
            }
        }

    }

}
private data class  TC(val a:Int,val boolean: Boolean){}
@Composable
fun ListaOperazioniFatte(navController: NavHostController,voci: ArrayList<Operation>,) {
    Column(modifier = Modifier.run {
        padding(SmallPadding)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(9.dp))
            .fillMaxWidth()
            .height(450.dp)
    }
    ) {
        Row (modifier = Modifier.padding(SmallPadding)){
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.data))

                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "ordine ascendente")

            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.descrizione))

            }
            Button(onClick = { /*TODO*/ }) {

                Text(text = stringResource(id = R.string.importo))
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "ordine ascendente")
            }
        }
        val t:ArrayList<TC> = arrayListOf(
            TC(1,true),

            TC(2,true),
        )

        for (i in voci) {
            Card (
                modifier = Modifier
                    .padding(SmallPadding)
                    .height(60.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Row(modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        Log.i("Liste_fatte", "${i.id}")
                        navController.navigate("dettagli-operazione/" + i.id)
                    }
                ) {
                    val modifier = Modifier.padding(16.dp,18.dp, 2.dp, 4.dp)
                    Text(
                        text = i.dateO.toLocalDate().toString(),
                        fontSize = 13.sp,
                        modifier = modifier
                    )

                    //Spacer(modifier = Modifier.padding(SmallPadding))
                    Text(
                        text = i.description,
                        fontSize = 13.sp,
                        modifier = modifier
                    )
                    //Spacer(modifier = Modifier.padding(SmallPadding))
                    Text(
                        text = if(i.operationType == TransactionType.WITHDRAWAL) {"-"} else {"+"} + i.amount.toString(),
                        fontSize = 13.sp,
                        modifier = modifier
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContiPreview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Conti(navController,viewModel = viewModel)
        }
    }
}

