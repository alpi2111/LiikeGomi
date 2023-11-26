package com.liike.liikegomi.background.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.liike.liikegomi.background.database.converters.CategoryConverter
import com.liike.liikegomi.background.database.converters.RolConverter
import com.liike.liikegomi.background.database.dao_interfaces.*
import com.liike.liikegomi.background.database.entities.*

@Database(
    version = 2,
    entities = [
        Direcciones::class,
        Rol::class,
        Categoria::class,
        Productos::class,
        Usuarios::class,
        Ventas::class,
        VentasDetalle::class,
        CarritoUsuarios::class,
        ClienteDireccion::class
    ]
)
@TypeConverters(RolConverter::class, CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun direccionesDao(): DireccionesDao
    abstract fun rolDao(): RolDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun productosDao(): ProductosDao
    abstract fun usuariosDao(): UsuariosDao
    abstract fun ventasDao(): VentasDao
    abstract fun ventasDetalleDao(): VentasDetalleDao
    abstract fun carritoUsuariosDao(): CarritoUsuariosDao
    abstract fun clienteDireccionDao(): ClienteDireccionDao
}