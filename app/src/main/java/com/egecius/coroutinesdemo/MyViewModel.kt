package com.egecius.coroutinesdemo

import androidx.lifecycle.*

class MyViewModel : ViewModel() {

    private val fakeRepo = FakeRepo()

    private val selectedItemId = MutableLiveData("1")

    // you can use switchMap to map to another liveData object
    val result: LiveData<FakeItem> = selectedItemId.switchMap {
        liveData { emit(fetchItem()) }
    }

    private suspend fun fetchItem(): FakeItem {
        return fakeRepo.fetchItem()
    }

}
