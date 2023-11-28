package com.liike.liikegomi.background.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptUtils {
    private const val algorithm = "AES/CBC/PKCS5Padding"
    private val secretKeySpec = SecretKeySpec("L1i43E_60m1.A!(J".toByteArray(), "AES")
    private val ivParameterSpec = IvParameterSpec(ByteArray(16))

    fun encrypt(inputText: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val cipherText = cipher.doFinal(inputText.toByteArray())
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val plainText = cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP))
        return String(plainText)
    }
}