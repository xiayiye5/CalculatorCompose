package com.yhsh.flowstudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yhsh.flowstudy.bean.PersonRoom
import com.yhsh.flowstudy.dao.PersonRoomDao

@Database(entities = [PersonRoom::class], version = 2)
abstract class MyDataBase : RoomDatabase() {
    companion object {
        private var dbName = "nameDb"
        private var db: MyDataBase? = null

        // 定义从版本 1 到 2 的迁移
        private val migrationOneFromTwo = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 执行 SQL 添加 address 列
                database.execSQL(
                    "ALTER TABLE PersonDb ADD COLUMN address TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getDb(context: Context) = if (db == null) {
            Room.databaseBuilder(context, MyDataBase::class.java, dbName)
                .addMigrations(migrationOneFromTwo).build().apply {
                    db = this
                }
        } else {
            db
        }
    }

    abstract fun getPersonRoomDao(): PersonRoomDao
}