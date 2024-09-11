package com.example.ashborn.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ashborn.Converters
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.User


@Database(
    entities = [User::class, Operation::class, Conto::class, Carta::class, Avviso::class],
    version = 4,
    exportSchema = true,
    /*autoMigrations = [

        AutoMigration(
            from = 1,
            to = 2,
            spec = AshbornDb.Migration1To2::class
        )
    ],*/

)
@TypeConverters(Converters::class)
abstract class AshbornDb:RoomDatabase() {
   abstract fun ashbornDao(): AshbornDao
   companion object{
       @Volatile
       private var Instance: AshbornDb? = null
       fun getDatabase(context: Context): AshbornDb {
           return Instance ?: synchronized(this) {
               Room.databaseBuilder(
                   context,
                   AshbornDb::class.java,
                   "Ashborn_db"
               )
                   .createFromAsset("Ashborn_db.db")
                   .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                   .fallbackToDestructiveMigration()
                   .enableMultiInstanceInvalidation()
                   .build()
                   .also { Instance = it }
           }
       }
   }
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE operations ADD COLUMN operationType TEXT not null")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE operations ADD COLUMN operationStatus TEXT NOT NULL DEFAULT 'DONE'")
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        //FIXME: non funziona ancora correttamente
        db.execSQL(
            """
            BEGIN;
            ALTER TABLE operations ADD COLUMN cardCode2 INTEGER REFERENCES carte(nrCarta) DEFAULT NULL;
            UPDATE operations SET cardCode2 = cast(cardCode AS INTEGER);
            ALTER TABLE operations DROP COLUMN cardCode;
            ALTER TABLE operations RENAME COLUMN cardCode2 TO cardCode;
            ALTER TABLE carte ADD COLUMN plafond DOUBLE NOT NULL DEFAULT 1500.0
            END;
            """.trimIndent()
        )
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