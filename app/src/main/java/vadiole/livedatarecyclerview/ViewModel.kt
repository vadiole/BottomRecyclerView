package vadiole.livedatarecyclerview

import android.app.Application
import android.content.ClipData
import android.util.Log
import androidx.core.util.rangeTo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import vadiole.livedatarecyclerview.model.Equatable
import vadiole.livedatarecyclerview.model.Item1
import vadiole.livedatarecyclerview.model.Item2
import kotlin.random.Random

class ViewModel(application: Application) : AndroidViewModel(application) {
    val list: LiveData<List<Equatable>> = getList().asLiveData()
    var mutableList = mutableListOf<Equatable>(Item1(1, "ss", "s"))
    private fun getList() = flow {
        for (i in 0..12) {
            val item = Item1(i, "Item $i", "time")
            val newPosition = Random.nextInt(0, mutableList.size)
            mutableList.add(newPosition, item)
            emit(mutableList)
            Log.i("ViewModel", "getList: emit")
            delay(0)
        }
    }

}
