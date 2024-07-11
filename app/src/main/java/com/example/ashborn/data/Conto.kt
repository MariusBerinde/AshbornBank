package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "conti",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["clientCode"],
            childColumns = ["codCliente"],
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Conto (
    @PrimaryKey
    val codConto:String ,
    val codCliente: String,
    val saldo:Double,
    val iban:String,
    val stato: Stato
)