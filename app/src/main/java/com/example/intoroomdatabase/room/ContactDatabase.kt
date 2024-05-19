package com.example.intoroomdatabase.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.intoroomdatabase.Convertor
import java.util.Objects

@Database(entities = [Contact::class], version = 2)
@TypeConverters(Convertor::class)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDOA

    companion object {
        val migration_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE contact ADD COLUMN isActive INTEGER NOT NULL DEFAULT(1)")
            }

        }

        // Singleton prevents multiple instances of database opening at the same time.
        // Volatile - writes to this field are immediately made visible to other threads.
        @Volatile //The value of the volatile variable is never cached, and all writes and reads will be done to and from the main memory.
        private var INSTANCE: ContactDatabase? = null
        fun getDatabase(context: Context): ContactDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {// This is to prevent multiple threads from accessing the database at the same time
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDatabase::class.java,
                        "contactDB"
                    ).addMigrations(migration_1_2).build()
                }
            }
            return INSTANCE!!
        }
    }
}