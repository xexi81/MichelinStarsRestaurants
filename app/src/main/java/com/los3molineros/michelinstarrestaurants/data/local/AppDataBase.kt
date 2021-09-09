package com.los3molineros.michelinstarrestaurants.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.los3molineros.michelinstarrestaurants.data.model.RestaurantEntity

@Database(entities = [RestaurantEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        private var INSTANCE: AppDataBase? = null
        fun getDatabase(context: Context): AppDataBase{
            INSTANCE = INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDataBase::class.java,"restaurant_tables").build()
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}