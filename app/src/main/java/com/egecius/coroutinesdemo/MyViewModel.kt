package com.egecius.coroutinesdemo

import androidx.lifecycle.*
import kotlinx.coroutines.delay

class MyViewModel : ViewModel() {

    private val selectedItemId = MutableLiveData("1")

    // you can use switchMap to map to another liveData object
    val result: LiveData<FakeItem> = selectedItemId.switchMap {
        liveData { emit(fetchItem()) }
    }

    private suspend fun fetchItem(): FakeItem {
        delay(100)
        return FakeItem()
    }

}
