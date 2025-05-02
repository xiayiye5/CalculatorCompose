package com.yhsh.flowstudy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yhsh.flowstudy.bean.PersonRoom
import com.yhsh.flowstudy.bean.SearchKeyWorld
import com.yhsh.flowstudy.dao.PersonRoomDao
import com.yhsh.flowstudy.dao.SearchKeyWorldDao

@Database(entities = [PersonRoom::class, SearchKeyWorld::class], version = 3)
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
        private val migrationTwoFromThree = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 执行 SQL 添加 address 列
                // 执行创建新表的 SQL
                database.execSQL(
                    """CREATE TABLE IF NOT EXISTS `SearchHistory` (
                `key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 0,
                `keyWorld` TEXT NOT NULL,
                `searchTime` TEXT NOT NULL
            )
        """.trimIndent()
                )
            }
        }

        fun getDb(context: Context) = if (db == null) {
            Room.databaseBuilder(context, MyDataBase::class.java, dbName)
                .addMigrations(migrationOneFromTwo).addMigrations(migrationTwoFromThree).build()
                .apply {
                    db = this
                }
        } else {
            db
        }
    }

    abstract fun getPersonRoomDao(): PersonRoomDao
    abstract fun getHistoryDao(): SearchKeyWorldDao
}