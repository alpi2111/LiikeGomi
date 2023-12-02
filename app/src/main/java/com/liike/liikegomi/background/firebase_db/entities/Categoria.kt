package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.database.types.CategoryType
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Categoria(
    @Exclude
    val categoryType: CategoryType,
    @get:PropertyName("is_visible")
    @set:PropertyName("is_visible")
    var isVisible: Boolean,
    @get:PropertyName("id_categoria")
    @set:PropertyName("id_categoria")
    var idCategory: Int = 0,
) : FirebaseObjectInterface {
    @get:PropertyName("categoria")
    @set:PropertyName("categoria")
    var category: String = categoryType.value

    constructor() : this(CategoryType.NATURAL, true, 0)
}
