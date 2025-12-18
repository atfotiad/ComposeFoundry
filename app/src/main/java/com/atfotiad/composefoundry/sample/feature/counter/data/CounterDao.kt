package com.atfotiad.composefoundry.sample.feature.counter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {

    // ⚡ Reactive Stream: Emits updates automatically when DB changes
    @Query("SELECT * FROM counter_table WHERE id = :id")
    fun getCounterFlow(id: String): Flow<CounterEntity?>

    // One-shot fetch (useful for initialization logic)
    @Query("SELECT * FROM counter_table WHERE id = :id")
    suspend fun getCounterSnapshot(id: String): CounterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCounter(entity: CounterEntity)
}
