package com.liike.liikegomi.background.database.dao_interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import com.liike.liikegomi.background.database.entities.ClienteDireccion

@Dao
interface ClienteDireccionDao {
    @Transaction
    @Insert
    suspend fun insert(clienteDireccion: ClienteDireccion)
}