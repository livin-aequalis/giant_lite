package com.example.demoewallet.app.rpc



class FearlessException(
    val kind: Kind,
    message: String?,
    exception: Throwable? = null
) : RuntimeException(message, exception) {

    enum class Kind {
        NETWORK,
        UNEXPECTED
    }

    companion object {

        fun networkError( throwable: Throwable): FearlessException {
            return FearlessException(Kind.NETWORK, "", throwable) // TODO: add common error text to resources
        }

        fun unexpectedError(exception: Throwable): FearlessException {
            return FearlessException(Kind.UNEXPECTED, exception.message ?: "", exception) // TODO: add common error text to resources
        }
    }
}
