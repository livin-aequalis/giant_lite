package com.example.demoewallet.app.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun Lifecycle.onDestroy(action: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            action()

            removeObserver(this)
        }
    })
}
