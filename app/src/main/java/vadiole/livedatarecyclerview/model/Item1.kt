package vadiole.livedatarecyclerview.model

import androidx.lifecycle.LiveData

data class Item1(val title: String, var time: LiveData<String>) {
}