package com.atfotiad.composefoundry.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atfotiad.composefoundry.sample.feature.counter.data.CounterDao
import com.atfotiad.composefoundry.sample.feature.counter.data.CounterEntity

@Database(entities = [CounterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}
