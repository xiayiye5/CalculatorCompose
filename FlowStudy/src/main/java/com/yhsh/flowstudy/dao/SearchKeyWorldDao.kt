package com.yhsh.flowstudy.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yhsh.flowstudy.bean.SearchKeyWorld

@Dao
interface SearchKeyWorldDao {
    @Query("select * from SearchHistory")
    fun getAllHistory(): List<SearchKeyWorld>

    @Insert
    fun insertHistory(searchKeyWorld: SearchKeyWorld)

    //删除指定的历史记录
    @Delete
    fun delete(searchKeyWorld: SearchKeyWorld)

    //删除多条历史记录
    @Delete
    fun deletes(list: List<SearchKeyWorld>)

    //清空所有的历史记录
    @Query("delete from searchHistory")
    fun clearAllHistory();
}