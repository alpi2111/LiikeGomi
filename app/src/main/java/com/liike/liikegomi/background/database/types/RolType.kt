package com.liike.liikegomi.background.database.types

sealed class RolType(val value: String) {
    data object USER: RolType("user")
    data object ADMIN: RolType("admin")
}
