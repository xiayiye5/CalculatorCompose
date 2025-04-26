package com.yhsh.flowstudy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yhsh.flowstudy.bean.PersonRoom

@Dao
interface PersonRoomDao {
    //查询所有的数据
    @Query("select * from PersonDb")
    fun getAll(): List<PersonRoom>

    //根据传入的名称查询 使用:来查询传入的名字
    @Query("select * from PersonDb where account=:name")
    fun getPersonByName(name: String): List<PersonRoom>

    //插入数据
    @Insert
    fun insertAccount(p: PersonRoom)
}