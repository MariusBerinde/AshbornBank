package com.example.ashborn.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun OperazioniDisponibili(navController: NavHostController, viewModel: AshbornViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Custom Top Bar
        val icons: ArrayList<ImageVector>  = arrayListOf(
            ImageVector.vectorResource(R.drawable.bank),
            ImageVector.vectorResource(R.drawable.credit_card_outline),
            ImageVector.vectorResource(R.drawable.currency_eur),
            ImageVector.vectorResource(R.drawable.chat_outline),
            ImageVector.vectorResource(R.drawable.dots_horizontal)
        )
        val index=0
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }

        Text(text = "Raw")
    }
}

fun gestisciErrori(
    beneficiario: String,
    iban: String,
    importo: String,
    causale: String,
    dataAccredito: String,
    viewModelOp: OperationViewModel
) {
    viewModelOp.setErroreBeneficiarioX(!viewModelOp.formatoBeneficiarioValido(beneficiario))
    viewModelOp.setErroreIbanX(!viewModelOp.formatoIbanValido(iban))
    viewModelOp.setErroreImportoX(!viewModelOp.formatoImportoValido(importo))
    viewModelOp.setErroreCausaleX(!viewModelOp.formatoCausaleValida(causale))
    viewModelOp.setErroreDataAccreditoX(!viewModelOp.formatoDataAccreditoValida(dataAccredito))


}

@Composable
fun Bonifico(navController: NavHostController, viewModel: AshbornViewModel,viewModelOp: OperationViewModel){
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                val maxLength = 100
                OutlinedTextField(
                    value = beneficiario,
                    onValueChange = { if (it.length <= maxLength) { beneficiario = it } },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.beneficiario)) }
                )
            }
            Row () {
                val maxLength = 27
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
                    onValueChange = { importo = it },
                    modifier = modifier,
                    label = { Text(stringResource(id = R.string.importo)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row () {
                val maxLength = 300
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
                    onClick = {
                        if(viewModelOp.formatoBonificoValido(beneficiario, iban, importo, causale, dataAccredito)) {
                            viewModelOp.setBeneficiarioX(beneficiario)
                            viewModelOp.setIbanX(iban)
                            viewModelOp.setImportoX(importo)
                            viewModelOp.setCausaleX(causale)
                            viewModelOp.setDataAccreditoX(dataAccredito)
                            navController.navigate("riepilogo")
                        } else {
                            gestisciErrori(beneficiario, iban, importo, causale, dataAccredito, viewModelOp)
                        }
                    },
                    modifier = modifier
                ) {
                    Text(text = stringResource(id = R.string.continua))
                }
            }
        }
    }
}

@Composable
fun PINBonifico(navController: NavHostController, viewModel: OperationViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(id = R.string.pin))
        }
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedTextField(
                value = viewModel.pin,
                onValueChange = {viewModel.setPinX(it)}
            )
        }
        Spacer(modifier = Modifier.height(MediumPadding))
        for (i in (0 .. 2)) {
            Row(
                // modifier = Modifier.padding(SmallPadding)
            ) {
                for (j in (0 .. 2)) {

                    Spacer(modifier = Modifier.width(SmallVerticalSpacing))
                    //     Spacer(modifier = Modifier.height(MediumPadding))
                    Button(
                        modifier = Modifier.size(70.dp,40.dp),
                        onClick = {
                            if(viewModel.pin.length <= 8) {
                                viewModel.setPinX(viewModel.pin + (3*i+j).toString())
                            }
                        }
                    ) {
                        Text(text = (3*i+j+1).toString())
                    }
                }
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
        Row (){
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp,40.dp),
                onClick = {
                    if(!viewModel.checkPin()) {
                        viewModel.set_StartDest("bonifico-effettuato")
                        navController.navigate("bonifico-effettuato") {popUpTo("bonifico-effettuato")}
                    } else {
                        viewModel.incrementWrongAttempts()
                    }
                }
            ) {
                Text(text = "OK")
            }
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp,40.dp),
                onClick = {if(viewModel.pin.length <= 8) {viewModel.setPinX(viewModel.pin + "0")}}) {
                Text(text = "0")
            }
            Spacer(modifier = Modifier.width(SmallVerticalSpacing))
            Button(
                modifier = Modifier.size(70.dp,40.dp),
                onClick = {if(viewModel.pin.isNotEmpty()) {viewModel.setPinX(viewModel.pin.dropLast(1))} }) {
                //Text(text = "<xI")
                Icon( Icons.Filled.Clear, contentDescription = "icona cancellazione")
            }
        }
    }
}

@Composable
fun RiepilogoBonifico(navController : NavHostController, viewModel : OperationViewModel){
    val modifier = Modifier.padding(LargePadding)
    Column (modifier = Modifier.padding(LargePadding)) {
        Text(text = stringResource(id = R.string.riepilogo_bonifico), fontSize = 40.sp)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.ordinante), fontWeight = FontWeight.Bold)
        Text(text = viewModel.codConto)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.beneficiario), fontWeight = FontWeight.Bold)
        Text(text = viewModel.beneficiario)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.iban), fontWeight = FontWeight.Bold)
        Text(text = viewModel.iban)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.importo), fontWeight = FontWeight.Bold)
        Text(text = viewModel.importo)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.causale), fontWeight = FontWeight.Bold)
        Text(text = viewModel.causale)
        Spacer(modifier = modifier)
        Text(text = stringResource(id = R.string.dataAccredito), fontWeight = FontWeight.Bold)
        Text(text = viewModel.dataAccredito)
        Spacer(modifier = modifier)
        Button(
            onClick = { navController.navigate("conferma-bonifico") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}

@Composable
fun BonificoConfermato(navController : NavHostController, viewModel : OperationViewModel) {
    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = stringResource(id = R.string.bonifico), fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.padding(top = 150.dp))
        Row () {
            Text(text = stringResource(id = R.string.conferma_operazione), fontSize = 20.sp)
        }
        Row () {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.emoticon),
                modifier = Modifier.size(256.dp),
                tint = Color.Yellow,
                contentDescription = "smile")
        }
        Row () {
            Button(onClick = { navController.navigate("bonifico") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.altro_bonifico))
            }
        }
        Row () {
            Button(onClick = { navController.navigate("conti") }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.chiudi))
            }
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
    val beneficiarioOK: Boolean = beneficiarioValido(beneficiario)
    val ibanOK: Boolean = ibanValido(iban)
    val importoOK: Boolean = importoValido(importo)
    val causaleOK: Boolean = causaleValida(causale)
    val dataAccreditoOK: Boolean = dataAccreditoValida(dataAccredito)
    return beneficiarioOK && ibanOK && importoOK && causaleOK && dataAccreditoOK
}

/**
 * @param beneficiario il beneficiario del bonifico deve contenere nome e cognome del beneficiario o il nome di un'azienda
 * @return true se il beneficiario è valido false altrimenti
 */
fun beneficiarioValido(beneficiario: String): Boolean {
    return true
}

/**
 * @param iban l'iban da controllare
 * @return true se l'iban è valido false altrimenti
 */
fun ibanValido(iban: String): Boolean {
    return true
}

/**
 * @param causale la causale del bonifico da effettuare
 * @return true se la causale è valida false altrimenti
 */
fun causaleValida(causale: String): Boolean {
    return true
}

/**
 * @param dataAccredito la data in cui verrà effettuato il bonifico
 * @return true se la data è valida false altrimenti
 */
fun dataAccreditoValida(dataAccredito: String): Boolean {
    return true
}

/**
 * @param importo l'importo del bonifico da effettuare
 * @return true se l'importo è valido false altrimenti
 */
fun importoValido(importo: String): Boolean {
    return true
}

@Preview(showBackground = true)
@Composable
fun PreviewO() {
    val viewModel = AshbornViewModel()
    val viewModelOp = OperationViewModel()
    val navController = rememberNavController()
    AshbornTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            //OperazioniDisponibili (navController = navController, viewModel =viewModel )
            //Bonifico(navController = navController, viewModel = viewModel,viewModelOp = viewModelOp)
            //BonificoConfermato(navController = navController, viewModel = viewModelOp)
            RiepilogoBonifico(navController = navController, viewModel = viewModelOp)
        }
    }
}
