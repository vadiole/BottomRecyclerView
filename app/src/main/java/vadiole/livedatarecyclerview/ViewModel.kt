package vadiole.livedatarecyclerview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import vadiole.livedatarecyclerview.list.Equatable
import vadiole.livedatarecyclerview.list.Item1
import vadiole.livedatarecyclerview.list.Item2
import kotlin.random.Random

class ViewModel(application: Application) : AndroidViewModel(application) {
    val list: LiveData<List<Equatable>> = getList().asLiveData()
    var mutableList = mutableListOf<Equatable>(Item2(1))
    private fun getList() = flow {
        for (i in 0..20) {
            val item = Item1(i, "Item $i", "time")
            val newPosition = Random.nextInt(0, mutableList.size)
            mutableList.add(newPosition, item)
            emit(mutableList)
            Log.i("ViewModel", "getList: emit")
            delay(0)
        }
    }

}
