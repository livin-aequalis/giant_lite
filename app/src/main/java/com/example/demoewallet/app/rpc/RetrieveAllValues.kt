package com.example.demoewallet.app.rpc

import com.example.demoewallet.app.rpc.BulkRetriever
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

suspend fun BulkRetriever.retrieveAllValues(socketService: SocketService, keyPrefix: String): Map<String, String?> {
    val allKeys = retrieveAllKeys(socketService, keyPrefix)

    return queryKeys(socketService, allKeys)
}
