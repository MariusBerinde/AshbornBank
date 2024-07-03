package com.example.ashborn.data

import androidx.room.Entity
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
    indices = [Index(value = ["clientCode"])]
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
    val operationType:TransactionType
)/*{
   open fun getValue(){}

}*/
