package com.liike.liikegomi.background.shared_prefs

@Suppress("ClassName")
sealed class SharedPreferenceKeys(val value: String) {
    data object USER_ID: SharedPreferenceKeys("user_id")
    data object IS_USER_LOGGED: SharedPreferenceKeys("is_user_logged")
    data object NAME_OF_USER: SharedPreferenceKeys("NAME_OF_USER".lowercase())
    data object LAST_NAME_USER: SharedPreferenceKeys("LAST_NAME_USER".lowercase())
    data object EMAIL_USER: SharedPreferenceKeys("EMAIL_USER".lowercase())
    data object PHONE_USER: SharedPreferenceKeys("PHONE_USER".lowercase())
    data object USER_IS_ADMIN: SharedPreferenceKeys("USER_IS_ADMIN".lowercase())
}
