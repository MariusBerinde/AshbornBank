package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import java.time.LocalDateTime

enum class Stato{ //todo:spostare in file enums
    ATTIVO,BLOCCATO,CANCELLATO
}
@Entity(tableName = "carte")
data class Carta (
    @PrimaryKey
    val nrCarta:Long,

    @TypeConverters(Converters::class)
    val dataScadenza: LocalDateTime,
    val cvc:String,
    //TODO: dovrebbe essere una foreign key
    val codUtente:String,
    val codConto: String,
//assunzione visto che trattiamo solo carte collegate a conti il saldo del conto è lo stesso della carta in questo caso  deve essere una foreign key
    var saldo:Double,
    val statoCarta: Stato
)