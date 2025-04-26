package com.yhsh.flowstudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yhsh.flowstudy.bean.PersonRoom
import com.yhsh.flowstudy.dao.PersonRoomDao

@Database(entities = [PersonRoom::class], version = 1)
abstract class MyDataBase : RoomDatabase() {
    companion object {
        private var dbName = "nameDb"
        private var db: MyDataBase? = null
        fun getDb(context: Context) = if (db == null) {
            Room.databaseBuilder(context, MyDataBase::class.java, dbName).build().apply {
                db = this
            }
        } else {
            db
        }
    }

    abstract fun getPersonRoomDao(): PersonRoomDao
}