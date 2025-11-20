package uk.ac.tees.mad.bloodbond.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MainEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maiDao(): MainDao
}