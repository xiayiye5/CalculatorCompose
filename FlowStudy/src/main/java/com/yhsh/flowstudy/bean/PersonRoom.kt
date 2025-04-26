package com.yhsh.flowstudy.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PersonDb")
data class PersonRoom(
    @ColumnInfo(name = "account") var name: String,
    //自动增长
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "xuehao") var age: Int
)
