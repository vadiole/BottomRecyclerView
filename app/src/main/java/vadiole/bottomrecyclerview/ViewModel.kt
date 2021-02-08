package vadiole.bottomrecyclerview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import vadiole.bottomrecyclerview.list.Equatable
import vadiole.bottomrecyclerview.list.Item1
import vadiole.bottomrecyclerview.list.Item2

class ViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    //  states
    var isAnimating by state.delegate(false)
    var isExtended by state.delegate(false)


    val list: LiveData<List<Equatable>> = getList().asLiveData()
    var mutableList = mutableListOf<Equatable>(Item2(1))
    private fun getList() = flow {
//        for (i in 0..20) {
//            val item = Item1(i, "Item $i", "time")
//            mutableList.add(i, item)
//            emit(mutableList)
//            Log.i("ViewModel", "getList: emit")
//            delay(100)
//        }


        for (i in 0..0) {
//            val newList = List<Equatable>(12) { Item2(it) }
            val newList = List<Equatable>(12) { Item1(it, "Item $it", "time") }
            delay(500)
            mutableList.addAll(0, newList)
            emit(mutableList)

        }


    }.flowOn(Dispatchers.IO)
}
