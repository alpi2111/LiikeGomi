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
        firestore.collection(USERS_DB_NAME).add(user).addOnSuccessListener(activity) { reference ->
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    private fun getUserFromUserNameQuery(userName: String): Query {
        val userEncrypted = CryptUtils.encrypt(userName.trim())
        return firestore.collection(USERS_DB_NAME).whereEqualTo("nombre_usuario", userEncrypted)
    }

    private fun isUserNameAvailable(activity: AppCompatActivity, userName: String, callback: (Boolean) -> Unit) {
        getUserFromUserNameQuery(userName).get().addOnSuccessListener(activity) { documents ->
            callback.invoke(documents.isEmpty)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    private fun isEmailAvailable(activity: AppCompatActivity, email: String, callback: (Boolean) -> Unit) {
        val emailEncrypted = CryptUtils.encrypt(email.trim())
        firestore.collection(USERS_DB_NAME).whereEqualTo("correo", emailEncrypted).get().addOnSuccessListener(activity) { documents ->
            callback.invoke(documents.isEmpty)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    fun userCanCreateAnAccount(activity: AppCompatActivity, userName: String, email: String, callback: (Boolean) -> Unit) {
        isUserNameAvailable(activity, userName) { isUserAvailable ->
            if (isUserAvailable) {
                isEmailAvailable(activity, email) { isEmailAvailable ->
                    callback.invoke(isEmailAvailable)
                }
            } else
                callback.invoke(false)
        }
    }

    fun loginWithUserNameAndPassword(activity: AppCompatActivity, userName: String, password: String, callback: (Boolean) -> Unit) {
        getUserFromUserNameQuery(userName).get().addOnSuccessListener(activity) {
            val documents = it.documents
            if (documents.isEmpty())
                callback.invoke(false)
            else {
                if (documents.size > 1) {
                    callback.invoke(false)
                } else {
                    val userId = documents.first().id
                    val userCopy = documents.first().toObject<Usuarios>()!!.copy(isLogged = true)
                    if (userCopy.password == CryptUtils.encrypt(password)) {
                        firestore.collection(USERS_DB_NAME).document(userId).set(userCopy.isLogged, SetOptions.merge())
                        callback.invoke(true)
                    } else
                        callback.invoke(false)
                }
            }
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

}