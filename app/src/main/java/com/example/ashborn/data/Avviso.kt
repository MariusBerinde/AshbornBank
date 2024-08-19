package com.example.ashborn.data
import androidx.room.Index
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity(
    tableName = "avvisi",
    indices = [Index(value = ["id"])],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["clientCode"],
            childColumns = ["destinatario"],
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

@Serializable
data class Avviso(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val titolo:String,
    val contenuto:String,
    @Serializable(with = LocalDateTimeSerializer::class)
    @TypeConverters(Converters::class)
    @Contextual
    val data:LocalDateTime,
    // deve essere un codice cliente
    val destinatario:String,
    var stato:StatoAvviso = StatoAvviso.DA_LEGGERE
)
enum class StatoAvviso{ LETTO,DA_LEGGERE,CANCELLATO}
