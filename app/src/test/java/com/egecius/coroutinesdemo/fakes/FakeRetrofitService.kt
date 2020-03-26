package com.egecius.coroutinesdemo.fakes

class FakeRetrofitService {

    fun getData(): FakeCall {
        return FakeCall()
    }

    suspend fun getDataAsCoroutine(): String {
        return getData().await()
    }
}