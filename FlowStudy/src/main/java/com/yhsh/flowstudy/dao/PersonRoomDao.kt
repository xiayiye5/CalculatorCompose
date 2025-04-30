package com.yhsh.flowstudy.dao

import androidx.room.Dao
import androidx.room.Delete
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

    //删除单条数据
    @Query("DELETE FROM PersonDb WHERE id = :personId")
    fun deleteId(personId: Int)

    //删除指定集合数据
    @Query("DELETE FROM PersonDb WHERE id IN (:ids)")
    fun deleteIds(ids: List<Int>)

    // 使用 @Delete 删除实体对象（必须传入完整对象）
    // --------------------------------------------------
    @Delete
    fun deletePerson(p: PersonRoom)  // 单条删除

    @Delete
    fun deletePersons(persons: List<PersonRoom>)  // 批量删除对象

    //清空所有数据
    @Query("DELETE FROM PersonDb")
    fun clear()
}