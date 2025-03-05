package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "platform", indices = [Index(value = ["name"], unique = true)])
data class Platform(
    @PrimaryKey(autoGenerate = true)
    val platformId: Long = 0L,
    val name: String
)




