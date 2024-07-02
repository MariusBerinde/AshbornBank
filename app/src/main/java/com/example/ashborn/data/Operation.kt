package com.example.ashborn.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["clientCode"],
            parentColumns = ["clientCode"]
        )
    ]
)

data class Operation(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "clientCode")
    val clientCode:String,
    @TypeConverters(Converters::class)
    val dateO: LocalDateTime,
    @TypeConverters(Converters::class)
    val dateV: LocalDateTime,
    val description:String,
    //val amount: CurrencyAmount,
    val amount: Double,
    val operationType:TransactionType
){
   open fun getValue(){}

}
