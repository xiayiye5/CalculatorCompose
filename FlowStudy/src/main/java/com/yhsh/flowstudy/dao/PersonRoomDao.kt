package com.yhsh.flowstudy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yhsh.flowstudy.bean.PersonRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonRoomDao {
    //查询所有的数据
    @Query("select * from PersonDb")
    fun getAll(): Flow<List<PersonRoom>>

    //根据传入的名称查询 使用:来查询传入的名字
    //模糊匹配 @Query("select * from PersonDb where account like '%' || :name || '%'")
    @Query("select * from PersonDb where account=:name")
    fun getPersonByName(name: String): Flow<List<PersonRoom>>

    //插入数据
    @Insert
    fun insertAccount(p: PersonRoom)
}