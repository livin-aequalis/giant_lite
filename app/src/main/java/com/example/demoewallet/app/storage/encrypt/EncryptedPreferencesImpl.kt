package com.example.demoewallet.app.storage.encrypt

import com.example.demoewallet.app.storage.Preferences

class EncryptedPreferencesImpl(
    private val preferences: Preferences,
    private val encryptionUtil: EncryptionUtil
) : EncryptedPreferences {

    override fun putEncryptedString(field: String, value: String) {
        preferences.putString(field, encryptionUtil.encrypt(value))
    }

    override fun getDecryptedString(field: String): String? {
        val encryptedString = preferences.getString(field)
        return encryptedString?.let { encryptionUtil.decrypt(it) }
    }

    override fun hasKey(field: String): Boolean {
        return preferences.contains(field)
    }

    override fun removeKey(field: String) {
        preferences.removeField(field)
    }
}
