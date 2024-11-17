package com.jeong.mapmo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.entities.MemoEntity
import com.jeong.mapmo.data.repository.MemoRepository
import com.jeong.mapmo.data.repository.MemoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MemoAddViewModel @Inject constructor(
//    private val repository: MemoRepositoryImpl
//) : ViewModel(){

class MemoAddViewModel : ViewModel(){

    val repository = MemoRepositoryImpl()


    fun saveMemo(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMemo(MemoEntity(
            title = memo.title,
            location = memo.location,
            longitude = memo.longitude,
            latitude = memo.latitude,
            detail = memo.detail,
            priority = memo.priority,
            checked = memo.checked
        ))
    }

    fun deleteMemo(title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMemo(title)
    }

}
