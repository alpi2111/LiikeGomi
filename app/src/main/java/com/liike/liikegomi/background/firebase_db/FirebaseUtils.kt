package com.liike.liikegomi.background.firebase_db

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.CryptUtils

object FirebaseUtils {

    private const val USERS_DB_NAME = "usuarios"
    private const val PRODUCTS_DB_NAME = "productos"
    private const val CATEGORIES_DB_NAME = "categorias"
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
                isEmailAvailable(activity, email) { isEmailAvailable, emailAvailableMessage ->
                    callback.invoke(isEmailAvailable, emailAvailableMessage)
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
                    val userCopy = documents.first().toObject<Usuarios>()!!.apply { isLogged = true }
                    if (userCopy.password == CryptUtils.encrypt(password)) {
                        if (userCopy.isActive) {
                            firestore.collection(USERS_DB_NAME).document(userId).set(userCopy, SetOptions.merge())
                            SharedPrefs.string(SharedPreferenceKeys.USER_ID, userId)
                            SharedPrefs.string(SharedPreferenceKeys.NAME_OF_USER, userCopy.name)
                            SharedPrefs.string(SharedPreferenceKeys.LAST_NAME_USER, userCopy.lastName)
                            SharedPrefs.string(SharedPreferenceKeys.EMAIL_USER, userCopy.email)
                            SharedPrefs.string(SharedPreferenceKeys.PHONE_USER, userCopy.phoneNumber)
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

    fun getUserDocumentReference(): DocumentReference? {
        val userId = SharedPrefs.string(SharedPreferenceKeys.USER_ID)
        if (userId != null)
            return firestore.collection(USERS_DB_NAME).document(userId)
        return null
    }

    fun closeCurrentSession(callback: (Boolean, String?) -> Unit) {
        val userId = getUserDocumentReference() ?: run {
            callback.invoke(false, "La sesión no pudo ser cerrada, intenta nuevamente")
            return
        }
        userId.get().addOnSuccessListener {
            val user = try {
                it.toObject<Usuarios>()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            user.let { usr ->
                if (usr != null)
                    firestore.collection(USERS_DB_NAME).document(userId.id).set(usr.apply { isLogged = false }, SetOptions.merge()).addOnSuccessListener {
                        callback.invoke(true, "Sesión cerrada")
                    }.addOnFailureListener {
                        callback.invoke(false, it.message ?: "Save user unknown error")
                    }
                else
                    callback.invoke(false, "Error en el usuario")
            }
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    fun addProduct(product: Productos, callback: (Boolean, String?) -> Unit) {
        firestore.collection(PRODUCTS_DB_NAME).add(product).addOnSuccessListener {
            callback.invoke(true, null)
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    fun getProductCategoriesListener(callback: (Boolean, List<Categoria>?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME).addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                callback.invoke(false, null, exception?.message ?: "Unknown error")
            } else {
                val categoryList = mutableListOf<Categoria>()
                snapshot.documents.forEach { document ->
                    val category = document.toObject(Categoria::class.java) ?: return@forEach
                    if (category.isVisible)
                        categoryList.add(category)
                }
                callback.invoke(true, categoryList.sortedBy { it.idCategory }, null)
            }
        }
    }

    fun getProductCategories(callback: (Boolean, List<Categoria>?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME).get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                callback.invoke(false, null, "No se encontraron categorías, añade una primero")
            } else {
                val categoryList = mutableListOf<Categoria>()
                snapshot.documents.forEach { document ->
                    val category = document.toObject(Categoria::class.java) ?: return@forEach
                    if (category.isVisible)
                        categoryList.add(category)
                }
                callback.invoke(true, categoryList.sortedBy { it.idCategory }, null)
            }
        }.addOnFailureListener {

        }
    }


    fun getLastProductCategoryId(callback: (Boolean, Int?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME)
            .orderBy("id_categoria", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty)
                    callback.invoke(true, 1, null)
                else {
                    val category = it.first().toObject(Categoria::class.java)
                    callback.invoke(true, category.idCategory + 1, null)
                }
            }.addOnFailureListener {
                callback.invoke(false, null, it.message ?: "Unknown error")
            }
    }

    fun getLastProductId(callback: (Boolean, Int?, String?) -> Unit) {
        firestore.collection(PRODUCTS_DB_NAME)
            .orderBy("id_producto", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty)
                    callback.invoke(true, 1, null)
                else {
                    val producto = it.first().toObject(Productos::class.java)
                    callback.invoke(true, producto.productId + 1, null)
                }
            }.addOnFailureListener {
                callback.invoke(false, null, it.message ?: "Unknown error")
            }
    }

    fun getProductsByCategory(idCategory: Int, callback: (Boolean, List<Productos>?, String?) -> Unit) {
        firestore.collection(PRODUCTS_DB_NAME).whereEqualTo("id_cat", idCategory)
            .orderBy("id_producto", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    callback.invoke(false, null, exception?.message ?: "Unknown error")
                } else {
                    if (snapshot.isEmpty)
                        callback.invoke(false, null, "No existen productos para ésta categoría")
                    else {
                        val products = mutableListOf<Productos>()
                        snapshot.documents.forEach { document ->
                            val product = document.toObject(Productos::class.java) ?: return@forEach
                            if (product.isVisible)
                                products.add(product)
                        }
                        callback.invoke(true, products, null)
                    }
                }
            }
    }

}