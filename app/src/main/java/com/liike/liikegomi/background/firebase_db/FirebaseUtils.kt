package com.liike.liikegomi.background.firebase_db

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.liike.liikegomi.background.database.types.RolType
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.CART_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.CART_USER_ID_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.CATEGORIES_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.CATEGORY_ID_CATEGORY_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.HELPERS_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.PRIVACY_TERMS_DB_DOCUMENT
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.PRIVACY_TERMS_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.PRODUCTS_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.PRODUCT_ID_CATEGORY_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.PRODUCT_ID_PRODUCT_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.SELLS_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.USERS_DB_NAME
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.USER_EMAIL_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.USER_TYPE_DB_FIELD
import com.liike.liikegomi.background.firebase_db.FirebaseConstants.USER_USERNAME_DB_FIELD
import com.liike.liikegomi.background.firebase_db.entities.*
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.CryptUtils
import kotlinx.coroutines.tasks.await

object FirebaseUtils {

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
        return firestore.collection(USERS_DB_NAME).whereEqualTo(USER_USERNAME_DB_FIELD, userEncrypted)
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
        firestore.collection(USERS_DB_NAME).whereEqualTo(USER_EMAIL_DB_FIELD, emailEncrypted).get().addOnSuccessListener(activity) { documents ->
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
                            val userIsAdmin = userCopy.rol[USER_TYPE_DB_FIELD] == RolType.ADMIN.value
                            SharedPrefs.bool(SharedPreferenceKeys.USER_IS_ADMIN, userIsAdmin)
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
                        categoryList.add(category.apply { idFirebaseCategory = document.id })
                }
                callback.invoke(true, categoryList.sortedBy { it.idCategory }, null)
            }
        }
    }

    fun getProductCategories(ignoreVisibility: Boolean, callback: (Boolean, List<Categoria>?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME).get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                callback.invoke(false, null, "No se encontraron categorías, añade una primero")
            } else {
                val categoryList = mutableListOf<Categoria>()
                snapshot.documents.forEach { document ->
                    val category = document.toObject(Categoria::class.java) ?: return@forEach
                    if (ignoreVisibility)
                        categoryList.add(category.apply { idFirebaseCategory = document.id })
                    else if (category.isVisible)
                        categoryList.add(category.apply { idFirebaseCategory = document.id })
                }
                callback.invoke(true, categoryList.sortedBy { it.idCategory }, null)
            }
        }.addOnFailureListener {
            callback.invoke(false, null, it.message ?: "Unknown error")
        }
    }


    fun getLastProductCategoryId(callback: (Boolean, Int?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME)
            .orderBy(CATEGORY_ID_CATEGORY_DB_FIELD, Query.Direction.DESCENDING)
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
            .orderBy(PRODUCT_ID_PRODUCT_DB_FIELD, Query.Direction.DESCENDING)
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

    fun getProductsByCategory(idCategory: Int, ignoreVisibility: Boolean, callback: (Boolean, List<Productos>?, String?) -> Unit) {
        firestore.collection(PRODUCTS_DB_NAME).whereEqualTo(PRODUCT_ID_CATEGORY_DB_FIELD, idCategory)
            .orderBy(PRODUCT_ID_PRODUCT_DB_FIELD, Query.Direction.ASCENDING)
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
                            product.productIdFirebase = document.id
                            if (ignoreVisibility)
                                products.add(product)
                            else if (product.isVisible)
                                products.add(product)
                        }
                        callback.invoke(true, products, null)
                    }
                }
            }
    }

    fun getLastCategoryId(callback: (Boolean, Int?, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME)
            .orderBy(CATEGORY_ID_CATEGORY_DB_FIELD, Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty)
                    callback.invoke(true, 1, null)
                else {
                    val categoria = it.first().toObject(Categoria::class.java)
                    callback.invoke(true, categoria.idCategory + 1, null)
                }
            }.addOnFailureListener {
                callback.invoke(false, null, it.message ?: "Unknown error")
            }
    }

    fun addCategory(categoria: Categoria, callback: (Boolean, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME).add(categoria).addOnSuccessListener {
            callback.invoke(true, null)
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    fun updateCategory(categoria: Categoria, callback: (Boolean, String?) -> Unit) {
        if (categoria.idFirebaseCategory.isBlank()) {
            callback.invoke(false, "The firebase id for this category is not valid")
            return
        }
        firestore.collection(CATEGORIES_DB_NAME).document(categoria.idFirebaseCategory).set(categoria, SetOptions.merge()).addOnSuccessListener {
            callback.invoke(true, null)
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    fun deleteCategory(idFirebaseCategory: String, callback: (Boolean, String?) -> Unit) {
        firestore.collection(CATEGORIES_DB_NAME).document(idFirebaseCategory).delete().addOnSuccessListener {
            callback.invoke(true, null)
        }.addOnFailureListener {
            callback.invoke(false, it.message ?: "Unknown error")
        }
    }

    suspend fun deleteAllProductsByCategory(idCategory: Int): Boolean {
        return try {
            val documents = firestore.collection(PRODUCTS_DB_NAME).whereEqualTo(PRODUCT_ID_CATEGORY_DB_FIELD, idCategory).get().await()
            documents.forEach { document ->
                firestore.collection(PRODUCTS_DB_NAME).document(document.id).delete().await()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteProduct(product: Productos): Boolean {
        return try {
            firestore.collection(PRODUCTS_DB_NAME).document(product.productIdFirebase).delete().await()
            true
        } catch (e: Exception) {
            Log.e("deleteProductError", e.message ?: "Unknown error")
            false
        }
    }

    suspend fun updateProduct(product: Productos): Boolean {
        return try {
            firestore.collection(PRODUCTS_DB_NAME).document(product.productIdFirebase).set(product, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e("deleteProductError", e.message ?: "Unknown error")
            false
        }
    }

    suspend fun getCartByUser(firebaseUserId: String): Carrito? {
        val cartData = firestore.collection(CART_DB_NAME).whereEqualTo(CART_USER_ID_DB_FIELD, firebaseUserId).get().await()
        if (cartData.documents.isEmpty()) {
            return null
        } else {
            if (cartData.documents.size > 1) {
                return Carrito()
            }
            val firstDocument = cartData.documents.first()
            val firstCart = firstDocument.toObject(Carrito::class.java) ?: return Carrito()
            return firstCart.apply { idFirebaseCarrito = firstDocument.id }
        }
    }

    suspend fun addProductToCart(firebaseUserId: String, product: Productos, quantity: Int = 1): Boolean {
        val cart = getCartByUser(firebaseUserId)
        val productCart = Carrito().apply {
            idFirebaseCarrito = null
            nombreUsuario = firebaseUserId
        }
        if (cart == null) {
            // NO CART EXIST
            productCart.productos = listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
            return try {
                firestore.collection(CART_DB_NAME).add(productCart).await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        if (cart.nombreUsuario.isNullOrBlank() or cart.idFirebaseCarrito.isNullOrBlank()) {
            // THERE'S MORE THAN ONE CART
            val safeCart = getCartAndDeleteOthersUserCarts(firebaseUserId)
            if (safeCart == null) {
                productCart.productos = listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
                return try {
                    firestore.collection(CART_DB_NAME).add(productCart).await()
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            } else {
                if (safeCart.productos == null)
                    safeCart.productos = listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
                else
                    safeCart.productos = safeCart.productos!! + listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
                return try {
                    firestore.collection(CART_DB_NAME).add(safeCart).await()
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
        }
        // SINGLE CART
        return try {
            if (cart.productos == null)
                cart.productos = listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
            else {
                val indexOfCurrentProduct = cart.productos!!.indexOfFirst { it.idProducto == product.productId }
                if (indexOfCurrentProduct != -1) {
                    val products = cart.productos!!.toMutableList()
                    val np = products[indexOfCurrentProduct]
                    np.cantidad = np.cantidad!! + quantity
                    products[indexOfCurrentProduct] = np
                    cart.productos = products
                } else {
                    cart.productos = cart.productos!! + listOf(Carrito.Producto(product.productName, quantity, product.productId, product.productPrice/*, product.productImage*/))
                }
            }
            firestore.collection(CART_DB_NAME).document(cart.idFirebaseCarrito!!).set(cart, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun getCartAndDeleteOthersUserCarts(firebaseUserId: String): Carrito? {
        return try {
            val carts = firestore.collection(CART_DB_NAME).whereEqualTo(CART_USER_ID_DB_FIELD, firebaseUserId).get().await()
            val firstCart = carts.documents.first()
            carts.forEach {
                firestore.collection(CART_DB_NAME).document(it.id).delete().await()
            }
            firstCart.toObject(Carrito::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun isCartEmpty(): Boolean {
        val id = SharedPrefs.string(SharedPreferenceKeys.USER_ID) ?: return true
        val cart = getCartByUser(id)
        return cart?.productos?.isEmpty() ?: true
    }

    suspend fun updateCart(cart: Carrito): Boolean {
        return try {
            firestore.collection(CART_DB_NAME).document(cart.idFirebaseCarrito!!).set(cart, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteItemProductCart(cart: Carrito): Boolean {
        return try {
            val cartIsNowEmpty = cart.productos.isNullOrEmpty()
            if (cartIsNowEmpty) {
                firestore.collection(CART_DB_NAME).document(cart.idFirebaseCarrito!!).delete().await()
            } else {
                firestore.collection(CART_DB_NAME).document(cart.idFirebaseCarrito!!).set(cart, SetOptions.merge()).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserAddress(): List<Direcciones> {
        val firebaseUser = SharedPrefs.string(SharedPreferenceKeys.USER_ID)
        return try {
            val user = firestore.collection(USERS_DB_NAME).document(firebaseUser!!).get().await().toObject(Usuarios::class.java)
            user!!.address ?: listOf()
        } catch (e: Exception) {
            listOf()
        }
    }

    suspend fun addAddress(direction: Direcciones): Boolean {
        val firebaseUser = SharedPrefs.string(SharedPreferenceKeys.USER_ID)
        return try {
            val user = firestore.collection(USERS_DB_NAME).document(firebaseUser!!).get().await().toObject(Usuarios::class.java)
            val directions = user!!.address?.toMutableList() ?: mutableListOf()
            directions.add(direction)
            user.address = directions
            firestore.collection(USERS_DB_NAME).document(firebaseUser).set(user, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getPrivacyTerms(): String {
        val data = firestore.collection(HELPERS_DB_NAME).document(PRIVACY_TERMS_DB_DOCUMENT).get().await()
        return data.get(PRIVACY_TERMS_DB_FIELD).toString()
    }

    suspend fun addSell(sell: Ventas): Boolean {
        return try {
            firestore.collection(SELLS_DB_NAME).add(sell).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteCart(carrito: Carrito): Boolean {
        return try {
            firestore.collection(CART_DB_NAME).document(carrito.idFirebaseCarrito!!).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteCart(cartId: String): Boolean {
        return try {
            firestore.collection(CART_DB_NAME).document(cartId).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}