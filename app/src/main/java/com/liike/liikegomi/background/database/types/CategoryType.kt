package com.liike.liikegomi.background.database.types

sealed class CategoryType(val value: String) {
    data object NATURAL: CategoryType("Naturales")
    data object ENCHILADO: CategoryType("Enchilados")
    data object VITAMINAS: CategoryType("Vitaminados")
}
