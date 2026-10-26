package com.alextruck.dayloom.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alextruck.dayloom.data.local.dao.ThreadDao
import com.alextruck.dayloom.data.local.dao.WeaveDao
import com.alextruck.dayloom.data.local.entity.EchoInteractionEntity
import com.alextruck.dayloom.data.local.entity.ThreadEntity
import com.alextruck.dayloom.data.local.entity.WeaveEntity

@Database(
    entities = [ThreadEntity::class, WeaveEntity::class, EchoInteractionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DayloomDatabase : RoomDatabase() {
    abstract fun threadDao(): ThreadDao
    abstract fun weaveDao(): WeaveDao

    companion object {
        @Volatile
        private var INSTANCE: DayloomDatabase? = null

        fun getDatabase(context: Context): DayloomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DayloomDatabase::class.java,
                    "dayloom_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
