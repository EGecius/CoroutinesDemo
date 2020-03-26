package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/** Mimics Retrofit's Call class */
class FakeCall {

    fun cancel() {
        print("FakeCall.cancel()")
    }

    fun enqueue(callBack: CallBack) {
        callBack.onResponse()
        print("FakeCall.enqueue()")
    }

    interface CallBack {
        fun onResponse()
        fun onFailure(exception: Exception)
    }
}

suspend fun FakeCall.await() {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }

        enqueue(object : FakeCall.CallBack {
            override fun onResponse() {
                print("FakeCall.onResponse")
//                continuation.resume()
            }

            override fun onFailure(exception: Exception) {
                print("FakeCall.onFailure")
                continuation.resumeWithException(exception)
            }
        })
    }
}