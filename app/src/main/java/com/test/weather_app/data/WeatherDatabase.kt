package com.test.weather_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.weather_app.model.WeatherModel

@Database(entities = [WeatherModel::class], exportSchema = false, version = 1)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        private const val DB_NAME = "weather_db"

        fun getAppDataBase(context: Context): WeatherDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
    abstract fun weatherDao():WeatherDao
}