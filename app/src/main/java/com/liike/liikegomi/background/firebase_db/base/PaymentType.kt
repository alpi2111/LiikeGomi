package com.liike.liikegomi.background.firebase_db.base

sealed class PaymentType(val value: String) {
    data object CASH: PaymentType("efectivo")
    data object TRANSFER: PaymentType("transferencia")
    data class OTHER(private val other: String): PaymentType(other)
}
