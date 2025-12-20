package com.atfotiad.composefoundry.features.counter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter_table")
data class CounterEntity(
    @PrimaryKey
    val id: String,
    val count: Int
)