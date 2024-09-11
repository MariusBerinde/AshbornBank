package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import java.time.LocalDateTime

enum class Stato{ //todo:spostare in file enums
    ATTIVO,BLOCCATO,CANCELLATO
}
@Entity(
    tableName = "carte",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["clientCode"],
            childColumns = ["codUtente"],
            onUpdate = ForeignKey.CASCADE
        ),
       ForeignKey(
        entity = Conto::class,
        parentColumns = ["codConto"],
        childColumns = ["codConto"],
        onUpdate = ForeignKey.CASCADE
       )
    ]
)
data class Carta (
    @PrimaryKey
    val nrCarta: Long,

    @TypeConverters(Converters::class)
    val dataScadenza: LocalDateTime,
    val cvc: String,
    val codUtente: String,
    val codConto: String,
    var saldo: Double, // Quanto è stato speso con quella carta mensilmente
    val statoCarta: Stato,
    val plafond: Double = 1500.0,
)