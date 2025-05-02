package com.yhsh.flowstudy.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
data class SearchKeyWorld(
    @ColumnInfo val keyWorld: String,
    @PrimaryKey(autoGenerate = true) val key: Int = 0,
    @ColumnInfo val searchTime: String
)