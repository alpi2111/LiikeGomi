package com.liike.liikegomi.background.database

import android.content.Context
import androidx.room.Room
import com.liike.liikegomi.background.database.entities.Direcciones
import com.liike.liikegomi.background.database.entities.Usuarios

object Dao {
    @Volatile
    private var db: AppDatabase? = null
    private val lock = Any()

    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "liike_gomi.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getInstance(): AppDatabase {
        synchronized(lock) {
            if (db == null)
                throw UninitializedPropertyAccessException("The app database is not initialized, please consider calling 'initDatabase(context) at some point after using it'")
            return db!!
        }
    }

    fun initDatabase(context: Context) {
        synchronized(lock) {
            if (db == null)
                buildDatabase(context).also { db = it }
        }
    }

    suspend fun saveDirection(direction: Direcciones) {
        getInstance().direccionesDao().insert(direction)
    }

    suspend fun saveUser(user: Usuarios): Boolean {
        val wasSaved = getInstance().usuariosDao().insert(user)
        return wasSaved == 1L
    }

    suspend fun getUserByUserName(userName: String, password: String): Usuarios? {
        return getInstance().usuariosDao().getUserByUserNameAndPassword(userName, password)
    }
}