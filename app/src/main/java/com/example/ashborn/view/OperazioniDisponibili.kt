package com.example.ashborn.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.max
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun OperazioniDisponibili(navController: NavHostController, viewModel: AshbornViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Custom Top Bar
        val icons: ArrayList<ImageVector>  = arrayListOf<ImageVector>(
            ImageVector.vectorResource(R.drawable.bank),
            ImageVector.vectorResource(R.drawable.credit_card_outline),
            ImageVector.vectorResource(R.drawable.currency_eur),
            ImageVector.vectorResource(R.drawable.chat_outline),
            ImageVector.vectorResource(R.drawable.dots_horizontal)
        )
       val index=0;
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Card(
                onClick = {
                         navController.navigate("bonifico")

            },
                modifier = Modifier.fillMaxWidth()
                ) {
                Row (
                    modifier = Modifier.padding(SmallPadding)
                ){

                    Icon(icons[index], contentDescription = "ll")
                     Spacer(modifier = Modifier.padding(SmallPadding))
                    Text(text = "Bonifico")
                }


            }
        }

        Spacer(modifier = Modifier
            .padding(SmallPadding)
            .fillMaxWidth())
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Card(
                onClick = {

                    navController.navigate("mav")

            },
                modifier = Modifier.fillMaxWidth()
                ) {
                Row (
                    modifier = Modifier.padding(SmallPadding)
                ){

                    Icon(icons[index], contentDescription = "ll")
                     Spacer(modifier = Modifier.padding(SmallPadding))
                    Text(text = "MAV/RAV")
                }


            }
        }
    }
}

@Composable
fun Mav(navController: NavHostController, viewModel: AshbornViewModel){
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Text(text = "Raw")
    }
}

fun gestisciErrori() {
    TODO("Not yet implemented")
}

@Composable
fun Bonifico(navController: NavHostController, viewModel: AshbornViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Text(text = "Bonifico")
        Column (){
            val modifier = Modifier
                .padding(SmallPadding)
                .fillMaxWidth()
            val ordinante = viewModel.codConto
            var beneficiario by remember { mutableStateOf("") }
            var iban by remember { mutableStateOf("") }
            var importo by remember { mutableStateOf("") }
            var causale by remember { mutableStateOf("") }
            var dataAccredito by remember { mutableStateOf("") }
            Row (){
                OutlinedTextField(
                    value = ordinante,
                    onValueChange = {},
                    modifier = modifier,
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.ordinante)) }
                )
            }
            Row () {
                val maxLength: Int = 100
                OutlinedTextField(
                    value = beneficiario,
                    onValueChange = { if (it.length <= maxLength) { beneficiario = it } },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.beneficiario)) }
                )
            }
            Row () {
                val maxLength: Int = 27
                OutlinedTextField(
                    value = iban,
                    onValueChange = { if(it.length <= maxLength) { iban = it } },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.iban)) }
                )
            }
            Row () {
                OutlinedTextField(
                    value = importo,
                    onValueChange = { importo = importoValido(it) },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.importo)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row () {
                val maxLength: Int = 300
                OutlinedTextField(
                    value = causale,
                    onValueChange = { if(it.length <= maxLength) { causale = it } },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.causale)) },

                )
            }
            Row () {
                OutlinedTextField(
                    value = dataAccredito,
                    onValueChange = { dataAccredito = it },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.dataAccredito)) },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO mostra date picker dialog*/ }) {
                            Icon(Icons.Filled.DateRange,
                            contentDescription = stringResource(id = R.string.scegliData),
                            tint = Color.Black)
                        }
                    }
                )
            }
            Row () {
                Button(
                    onClick = { if(bonificoValido(beneficiario, iban, importo, causale, dataAccredito)) {navController.navigate("riepilogo") } else { gestisciErrori() } },
                    modifier = modifier
                ) {
                    Text(text = stringResource(id = R.string.continua))
                }
            }
        }
    }
}

@Composable
fun RiepilogoBonifico(navController : NavHostController, viewModel : OperationViewModel ){
    val modifier = Modifier.padding(SmallPadding)
    Column {
        Text(text = stringResource(id = R.string.ordinante))
        Text(text = viewModel.codConto)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.beneficiario))
        Text(text = viewModel.beneficiario)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.iban))
        Text(text = viewModel.iban)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.importo))
        Text(text = viewModel.importo)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.causale))
        Text(text = viewModel.causale)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.dataAccredito))
        Text(text = viewModel.dataAccredito)
        Spacer(modifier = modifier)
        Button(onClick = { navController.navigate(""/*TODO implementare navigazione*/) }) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}

/**@param beneficiario il nome e il cognome del beneficiario
 * @param iban il codice iban del beneficiario
 * @param importo l'importo del bonifico in formato stringa
 * @param causale la motivazione del bonifico
 * @param dataAccredito la data in cui verrà effettuato il bonifico
 * @return true se il bonifico ha dati validi per effettuarlo false altrimenti
 */
fun bonificoValido(beneficiario: String, iban: String, importo: String, causale: String, dataAccredito: String): Boolean {
    val beneficiarioOK: Boolean = true
    val ibanOK: Boolean = true
    val importoOK: Boolean = true
    val causaleOK: Boolean = true
    val dataAccreditoOK: Boolean = true
    return beneficiarioOK && ibanOK && importoOK && causaleOK && dataAccreditoOK
}
/**
 * @brief extracts a valid amount from a string representation, use to avoid errors
 * @param importo is a string indicating an amount to be processed
 * @return an extracted valid amount in string format
 */
fun importoValido(importo: String): String {
    var importoValido: String = importo
    return importoValido
}

@Preview(showBackground = true)
@Composable
fun previewO() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            //OperazioniDisponibili (navController = navController, viewModel =viewModel )
            Bonifico(navController = navController, viewModel = viewModel)
        }
    }
}
