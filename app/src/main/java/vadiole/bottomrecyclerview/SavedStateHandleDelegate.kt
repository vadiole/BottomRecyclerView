package vadiole.bottomrecyclerview


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> SavedStateHandle.delegate(initialValue: T): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            val stateKey = property.name
            return this@delegate[stateKey] ?: initialValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            val stateKey = property.name
            this@delegate[stateKey] = value
        }
    }

inline fun <reified T> SavedStateHandle.livedata(key: String? = null, initialValue: T? = null) =
    ReadOnlyProperty<Any, MutableLiveData<T?>> { thisRef, property ->
        val stateKey = key ?: property.name
        if (initialValue == null) {
            this@livedata.getLiveData(stateKey)
        } else {
            this@livedata.getLiveData(stateKey, initialValue)
        }
    }