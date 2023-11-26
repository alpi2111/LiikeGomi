package com.liike.liikegomi.background.database.dao_interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.liike.liikegomi.background.database.entities.Usuarios

@Dao
interface UsuariosDao {
    @Transaction
    @Insert
    suspend fun insert(user: Usuarios): Long

    @Transaction
    @Query("SELECT * from usuarios WHERE nombre_usuario = :userName AND contra = :password LIMIT 1")
    suspend fun getUserByUserNameAndPassword(userName: String, password: String): Usuarios?
}