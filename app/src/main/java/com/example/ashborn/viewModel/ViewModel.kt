package com.example.ashborn.viewModel

import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AshbornViewModel(): ViewModel() {
    val IBAN: String = "IT1234567890123456789012345"
    var saldo: Double = 0.0
        private set
    var codConto: String = "0987654321"
        private set

    val tag = AshbornViewModel::class.java.simpleName
    var fistLogin: Boolean = false
    var startDest: String = "init"
        private set
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification

    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification

    var codCliente by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set // Optional: restrict external modification

    var cognome by mutableStateOf("")
        private set // Optional: restrict external modification

    var dataNascita by  mutableStateOf("")
        private set

    fun setPinX(pin: String) {
        this.pin = pin
    }

    fun setUserNameX(userName: String) {
        Log.i(tag,"valore username $userName")
        this.userName = userName
    }

    fun set_Cognome(cognome:String){
        this.cognome=cognome
        Log.i(tag,"valore cognome $cognome")
    }

    fun set_DataNascita(dataNascita:String){
        Log.i(tag,"valore data di Nascita $dataNascita")
        this.dataNascita= dataNascita
    }

    fun set_CodCliente(codCliente:String ){
        Log.i(tag,"valore codice cliente $codCliente")
        this.codCliente=codCliente
    }

    fun set_StartDest(startDest: String){
        if (startDest == "init" || startDest == "principale") {
            this.startDest= startDest
        }

    }
    fun checkPin(): Boolean {
        return false
    }

    fun incrementWrongAttempts() {
        this.wrongAttempts++
    }
}