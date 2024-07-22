package com.example.ashborn.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ashborn.Converters
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.User

@Database(
    entities = [User::class, Operation::class, Conto::class, Carta::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AshbornDb:RoomDatabase() {
   abstract fun ashbornDao(): AshbornDao
   // abstract val dao:UserDao
   companion object{
       @Volatile
       private var Instance:AshbornDb?=null
       fun getDatabase(context: Context): AshbornDb{
           return Instance?: synchronized(this){
               Room.databaseBuilder(
                   context,
                   AshbornDb::class.java,
                   "Ashborn_db"
               ).build().also { Instance = it }
           }
       }
   }

}

/*
addCallback(object : RoomDatabase.Callback(){
                   override fun onCreate(db: SupportSQLiteDatabase) {
                       super.onCreate(db)
                       db.execSQL(
                           """
                            CREATE TRIGGER aggiorna_saldo_carte
                            AFTER UPDATE ON conti
                            FOR EACH ROW
                            BEGIN
                                UPDATE carta
                                SET saldo = NEW.saldo
                                WHERE codConto = NEW.codConto;
                            END;
                        """.trimIndent()
                       )
                       db.execSQL(
                           """
                            CREATE TRIGGER aggiorna_saldo_conto
                            AFTER UPDATE ON carte
                            FOR EACH ROW
                            BEGIN
                                UPDATE conti
                                SET saldo = NEW.saldo
                                WHERE codConto = NEW.codConto;
                            END;
                        """.trimIndent()
                       )
                   }
               }).
 */