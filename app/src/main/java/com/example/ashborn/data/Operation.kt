package com.example.ashborn.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// todo: scrivere trafila per memorizzare in db
enum class TransactionType { DEPOSIT, WITHDRAWAL }

enum class OperationType{ MAV,WIRE_TRANSFER,CARD }

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
@Serializable
data class Operation(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
    @ColumnInfo("clientCode")val clientCode:String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class)
    @Contextual
    @ColumnInfo("dateO")   val dateO: LocalDateTime, //data ordinante (effettuazione operazione)
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class)
    @Contextual
    @ColumnInfo("dateV")   val dateV: LocalDateTime, // data valuta
    @ColumnInfo("description")  val description:String,
    @ColumnInfo("recipient")val recipient:String,
    //val amount: CurrencyAmount,
    @ColumnInfo("amount")val amount: Double,
    @ColumnInfo("transactionType") val transactionType: TransactionType,
    @ColumnInfo("bankAccount")  val bankAccount:String,
    @ColumnInfo("iban")val iban:String, //iban del destinatario  //se origine MAV iban contiene il codice Mav
    @ColumnInfo("cardCode") val cardCode:String?, // se nullo è un bonifico altrimenti è un movimento della carta
    @ColumnInfo("operationType ", defaultValue = "WIRE_TRANSFER")  val operationType:OperationType = OperationType.WIRE_TRANSFER
)/*{
   open fun getValue(){}

}*/

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(formatter)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, formatter)
    }
}
   
