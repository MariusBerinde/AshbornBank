package com.example.ashborn.data

import java.time.LocalDateTime
// todo: scrivere trafila per memorizzare in db
enum class TransactionType {
    DEPOSIT, WITHDRAWAL
}
data class Operation(
    val id: Long,
    val clientCode:String,
    val dateO: LocalDateTime,
    val dateV: LocalDateTime,
    val description:String,
    //val amount: CurrencyAmount,
    val amount: Double,
    val operationType:TransactionType
){
   open fun getValue(){}

}
