package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Categoria(
//    @Exclude
//    val categoryType: CategoryType,
    @get:PropertyName("categoria")
    @set:PropertyName("categoria")
    var category: String,
    @get:PropertyName("is_visible")
    @set:PropertyName("is_visible")
    var isVisible: Boolean,
    @get:PropertyName("id_categoria")
    @set:PropertyName("id_categoria")
    var idCategory: Int = 0,
) : FirebaseObjectInterface() {
    constructor() : this("", true, 0)

    override fun toString(): String = category
}
