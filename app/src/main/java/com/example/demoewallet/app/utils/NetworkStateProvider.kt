package com.example.demoewallet.app.utils

import androidx.lifecycle.MutableLiveData

class NetworkStateProvider : NetworkStateMixin {
    override val showConnectingBarLiveData = MutableLiveData(false)

    override fun updateShowConnecting(isShow: Boolean) {
        showConnectingBarLiveData.postValue(isShow)
    }
}
