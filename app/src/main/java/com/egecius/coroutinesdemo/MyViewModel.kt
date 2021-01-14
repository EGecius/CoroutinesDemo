package com.egecius.coroutinesdemo

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    fun startModelling() {
        demoInvokeOnCompletion()
    }

    private fun demoInvokeOnCompletion() {
        viewModelScope.launch {
            willFinishIn1s()
        }.invokeOnCompletion {
            Log.v("Eg:MyViewModel:31", "demoInvokeOnCompletion() it: $it")
        }
    }

    private suspend fun willFinishIn1s() {
        delay(1_000)
    }

    /** This shows how LiveData delegates to another LiveData */
    val resultFromSoruce: LiveData<FakeItem> = selectedItemId.switchMap {
        liveData { emitSource(fakeRepo.getLiveData()) }
    }
}
