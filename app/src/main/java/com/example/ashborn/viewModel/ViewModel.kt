package com.example.ashborn.viewModel

import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AshbornViewModel(): ViewModel() {
    var wrongAttempts by mutableIntStateOf(0)
        private set // Optional: restrict external modification

    var pin by mutableStateOf((""))
        private set // Optional: restrict external modification

    //var codCliente by mutableStateOf("")
     //   private set

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
        this.userName = userName
    }

    fun set_Cognome(cognome:String){
        this.cognome=cognome
        Log.i(this.javaClass.name,cognome)
    }

    fun set_DataNascita(dataNascita:String){
        this.dataNascita= dataNascita
    }

/*    fun setCodCliente(codCliente:String ){
      this.codCliente=codCliente
    }
*/

    fun checkPin(): Boolean {
        return false
    }

    fun incrementWrongAttempts() {
        this.wrongAttempts++
    }


}