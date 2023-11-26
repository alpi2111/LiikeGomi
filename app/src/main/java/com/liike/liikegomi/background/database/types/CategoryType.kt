package com.liike.liikegomi.background.database.types

sealed class CategoryType(val value: String) {
    data object NATURAL: CategoryType("naturales")
    data object ENCHILADO: CategoryType("enchilados")
    data object VITAMINAS: CategoryType("vitaminados")
}
