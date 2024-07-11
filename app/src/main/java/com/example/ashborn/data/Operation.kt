package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import java.time.LocalDateTime

// todo: scrivere trafila per memorizzare in db
enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}
@Entity(
    tableName = "operations",
    indices = [Index(value = ["clientCode"])],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["clientCode"],
            childColumns = ["clientCode"],
            onUpdate = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = Conto::class,
        parentColumns = ["codConto"],
        childColumns = ["bankAccount"],
        onUpdate = ForeignKey.CASCADE,
    )
    ]
)

data class Operation(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val clientCode:String,
    @TypeConverters(Converters::class)
    val dateO: LocalDateTime,
    @TypeConverters(Converters::class)
    val dateV: LocalDateTime,
    val description:String,
    //val amount: CurrencyAmount,
    val amount: Double,
    val operationType:TransactionType,
    val bankAccount:String,
    // se nullo è un bonifico altrimenti è un moviento della carta
    val cardCode:String?
)/*{
   open fun getValue(){}

}*/
