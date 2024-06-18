package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ConnectivityObserver
import com.example.ashborn.R
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.LargePadding
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.ui.theme.SmallPadding
import com.example.ashborn.ui.theme.SmallVerticalSpacing
import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel

@Composable
fun Operazioni(
    navController: NavHostController,
    viewModel: AshbornViewModel,
    connectionStatus: ConnectivityObserver.Status
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
        Surface(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)) {
            Text(text = stringResource(id = R.string.connessione_persa), textAlign = TextAlign.Center)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bonifico(navController: NavHostController , viewModelOp: OperationViewModel){
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
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
            Row (){
                OutlinedTextField(
                    value = viewModelOp.codConto,
                    onValueChange = {},
                    modifier = modifier,
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.ordinante)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Black
                    )
                )
            }
            Row () {
                val maxLength = 100
                OutlinedTextField(
                    value = viewModelOp.beneficiario,
                    onValueChange = { if (it.length <= maxLength) { viewModelOp.setBeneficiarioX(it) } },
                    label = { Text(stringResource(id = R.string.beneficiario)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if(viewModelOp.erroreBeneficiario == 1) {Red} else {Black},
                        unfocusedBorderColor = if(viewModelOp.erroreBeneficiario == 1) {Red} else {Black}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester2.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester1)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row () {
                val maxLength = 27
                OutlinedTextField(
                    value = viewModelOp.iban,
                    onValueChange = { if(it.length <= maxLength) { viewModelOp.setIbanX(it) } },
                    label = { Text(stringResource(id = R.string.iban)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if(viewModelOp.erroreIban == 1) {Red} else {Black},
                        unfocusedBorderColor = if(viewModelOp.erroreIban == 1) {Red} else {Black}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester3.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester2)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row () {
                OutlinedTextField(
                    value = viewModelOp.importo,
                    onValueChange = { viewModelOp.setImportoX(it) },
                    label = { Text(stringResource(id = R.string.importo)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if(viewModelOp.erroreImporto == 1) {Red} else {Black},
                        unfocusedBorderColor = if(viewModelOp.erroreImporto == 1) {Red} else {Black}
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester4.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester3)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row () {
                val maxLength = 300
                OutlinedTextField(
                    value = viewModelOp.causale,
                    onValueChange = { if(it.length <= maxLength) { viewModelOp.setCausaleX(it) } },
                    label = { Text(stringResource(id = R.string.causale)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if(viewModelOp.erroreCausale == 1) {Red} else {Black},
                        unfocusedBorderColor = if(viewModelOp.erroreCausale == 1) {Red} else {Black}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester5.requestFocus() }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester4)
                        .padding(SmallPadding)
                        .fillMaxWidth()
                )
            }
            Row () {
                OutlinedTextField(
                    value = viewModelOp.dataAccredito,
                    onValueChange = { viewModelOp.setDataAccreditoX(it) },
                    label = { Text(stringResource(id = R.string.dataAccredito)) },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO mostra date picker dialog*/ }) {
                            Icon(Icons.Filled.DateRange,
                            contentDescription = stringResource(id = R.string.scegliData),
                            tint = Color.Black)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if(viewModelOp.erroreDataAccredito == 1) {Red} else {Black},
                        unfocusedBorderColor = if(viewModelOp.erroreDataAccredito == 1) {Red} else {Black}
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if(viewModelOp.formatoBonificoValido(viewModelOp.beneficiario, viewModelOp.iban, viewModelOp.importo, viewModelOp.causale, viewModelOp.dataAccredito)) {
                                Log.i("Bonifico", "Sto per andare in riepilogo")
                                navController.navigate("riepilogo-bonifico")
                            } else {
                                gestisciErrori(viewModelOp.beneficiario, viewModelOp.iban, viewModelOp.importo, viewModelOp.causale, viewModelOp.dataAccredito, viewModelOp)
                            }
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester5)
                        .padding(SmallPadding)
                        .fillMaxWidth()

                )
            }
            Row () {
                Button(
                    onClick = {
                        if(viewModelOp.formatoBonificoValido(viewModelOp.beneficiario, viewModelOp.iban, viewModelOp.importo, viewModelOp.causale, viewModelOp.dataAccredito)) {
                            Log.i("Bonifico", "Sto per andare in riepilogo")
                            navController.navigate("riepilogo-bonifico")
                        } else {
                            gestisciErrori(viewModelOp.beneficiario, viewModelOp.iban, viewModelOp.importo, viewModelOp.causale, viewModelOp.dataAccredito, viewModelOp)
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
fun PINOperazione(navController: NavHostController, viewModel: OperationViewModel) {
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
                onValueChange = {viewModel.setPinX(it) },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation()
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
                        viewModel.set_StartDest("operazioni")
                        navController.navigate("operazioneConfermata") {popUpTo("operazioneConfermata")}
                    } else {
                        viewModel.incrementWrongAttempts()
                        if(viewModel.wrongAttempts >= 3){
                            viewModel.bloccaUtente()
                        }
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
    Log.i("RiepilogoBonifico", "Sono entrato")
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
            onClick = { navController.navigate("pin") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.continua))
        }
    }
}

@Composable
fun OperazioneConfermata(navController : NavHostController, viewModel : OperationViewModel) {
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
            Operazioni(
                navController = navController,
                viewModel =viewModel,
                connectionStatus = ConnectivityObserver.Status.Lost
            )
            //Bonifico(navController = navController, viewModelOp = viewModelOp)
            //BonificoConfermato(navController = navController, viewModel = viewModelOp)
            //RiepilogoBonifico(navController = navController, viewModel = viewModelOp)
        }
    }
}
