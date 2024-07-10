package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "conti")
data class Conto (
    @PrimaryKey
    val codConto:String ,
    val codCliente: String,
    val saldo:Double,
    val iban:String,
    val stato: Stato
)