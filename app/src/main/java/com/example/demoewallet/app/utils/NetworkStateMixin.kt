package com.example.demoewallet.app.utils

import androidx.lifecycle.LiveData

interface NetworkStateMixin : NetworkStateUi

interface NetworkStateUi {
    val showConnectingBarLiveData: LiveData<Boolean>

    fun updateShowConnecting(isShow: Boolean)
}
