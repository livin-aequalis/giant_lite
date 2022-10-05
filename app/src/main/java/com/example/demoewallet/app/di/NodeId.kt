package com.example.demoewallet.app.di

@JvmInline
value class NodeId(private val pair: Pair<String, String>) {

    val chainId: String
        get() = pair.first

    val nodeUrl: String
        get() = pair.second
}
