package com.jeong.mapmo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.entities.MemoEntity
import com.jeong.mapmo.data.repository.MemoRepository
import com.jeong.mapmo.data.repository.MemoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val repository: MemoRepositoryImpl
) : ViewModel() {

    fun saveMemo(memo: MemoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMemo(memo)
    }

    fun deleteMemo(memo: MemoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMemo(memo)
    }

    fun getMemo() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllMemo()
    }

}