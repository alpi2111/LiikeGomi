package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.database.types.CategoryType
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Categoria(
    @PropertyName("categoria")
    val category: CategoryType,
    @PropertyName("is_visible")
    val isVisible: Boolean,
    @PropertyName("id_categoria")
    val idCategory: Int = 0,
): FirebaseObjectInterface
