package com.atfotiad.composefoundry.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atfotiad.composefoundry.features.counter.data.local.CounterDao
import com.atfotiad.composefoundry.features.counter.data.local.CounterEntity

@Database(entities = [CounterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}