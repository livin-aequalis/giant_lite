package com.example.demoewallet.app.storage.encrypt

interface EncryptedPreferences {

    fun putEncryptedString(field: String, value: String)

    fun getDecryptedString(field: String): String?

    fun hasKey(field: String): Boolean

    fun removeKey(field: String)
}
