package com.egecius.coroutinesdemo

import kotlinx.coroutines.delay

class FakeRepo {
    suspend fun fetchItem(): FakeItem {
        delay(100)
        return FakeItem()
    }

}
