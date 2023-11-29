package com.liike.liikegomi.background.firebase_db

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.utils.CryptUtils

object FirebaseUtils {

    private const val USERS_DB_NAME = "usuarios"
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    fun saveUser(activity: AppCompatActivity, user: Usuarios, callback: (Boolean) -> Unit) {
        firestore.collection(USERS_DB_NAME).add(user).addOnSuccessListener(activity) {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    private fun getUserFromUserNameQuery(userName: String): Query {
        val userEncrypted = CryptUtils.encrypt(userName.trim())
        return firestore.collection(USERS_DB_NAME).whereEqualTo("nombre_usuario", userEncrypted)
    }

    private fun isUserNameAvailable(activity: AppCompatActivity, userName: String, callback: (Boolean, String?) -> Unit) {
        getUserFromUserNameQuery(userName).get().addOnSuccessListener(activity) { documents ->
            if (documents.isEmpty)
                callback.invoke(true, null)
            else
                callback.invoke(false, "Usuario existente")
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    private fun isEmailAvailable(activity: AppCompatActivity, email: String, callback: (Boolean, String?) -> Unit) {
        val emailEncrypted = CryptUtils.encrypt(email.trim())
        firestore.collection(USERS_DB_NAME).whereEqualTo("correo", emailEncrypted).get().addOnSuccessListener(activity) { documents ->
            if (documents.isEmpty)
                callback.invoke(true, null)
            else
                callback.invoke(false, "El correo ya ha sido registrado")
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    fun userCanCreateAnAccount(activity: AppCompatActivity, userName: String, email: String, callback: (Boolean, String?) -> Unit) {
        isUserNameAvailable(activity, userName) { isUserAvailable, message ->
            if (isUserAvailable) {
                isEmailAvailable(activity, email) { isEmailAvailable, message ->
                    callback.invoke(isEmailAvailable, message)
                }
            } else
                callback.invoke(false, message)
        }
    }

    fun loginWithUserNameAndPassword(activity: AppCompatActivity, userName: String, password: String, callback: (Boolean, String?) -> Unit) {
        getUserFromUserNameQuery(userName).get().addOnSuccessListener(activity) {
            val documents = it.documents
            if (documents.isEmpty())
                callback.invoke(false, "Usuario no registrado")
            else {
                if (documents.size > 1) {
                    callback.invoke(false, "Overflow search")
                } else {
                    val userId = documents.first().id
                    val userCopy = documents.first().toObject<Usuarios>()!!.copy(isLogged = true)
                    if (userCopy.password == CryptUtils.encrypt(password)) {
                        if (userCopy.isActive) {
                            firestore.collection(USERS_DB_NAME).document(userId).set(userCopy, SetOptions.merge())
                            callback.invoke(true, null)
                        } else {
                            callback.invoke(false, "El usuario no está activo, no puedes iniciar sesión")
                        }
                    } else
                        callback.invoke(false, "Contraseña incorrecta")
                }
            }
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

}