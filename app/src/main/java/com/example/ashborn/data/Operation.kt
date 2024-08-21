package com.example.ashborn.data

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
@Serializable
data class Operation(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val clientCode:String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class)
    @Contextual
    val dateO: LocalDateTime, //data ordinante (effettuazione operazione)
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class)
    @Contextual
    val dateV: LocalDateTime, // data valuta
    val description:String,
    val recipient:String,
    //val amount: CurrencyAmount,
    val amount: Double,
    val operationType:TransactionType,
    val bankAccount:String,
    val iban:String, //iban del destinatario
    val cardCode:String?, // se nullo è un bonifico altrimenti è un movimento della carta
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