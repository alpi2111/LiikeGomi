package com.liike.liikegomi.background.database.dao_interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.liike.liikegomi.background.database.entities.Rol
import com.liike.liikegomi.background.database.types.RolType

@Dao
interface RolDao {

    @Transaction
    @Insert
    suspend fun insert(rolDao: Rol)

    @Transaction
    @Query("SELECT id_rol FROM rol WHERE tipo = :rolType")
    suspend fun getRol(rolType: RolType = RolType.USER): Int?
}