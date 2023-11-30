package com.liike.liikegomi.background.shared_prefs

@Suppress("ClassName")
sealed class SharedPreferenceKeys(val value: String) {
    data object USER_ID: SharedPreferenceKeys("user_id")
    data object IS_USER_LOGGED: SharedPreferenceKeys("is_user_logged")
}
