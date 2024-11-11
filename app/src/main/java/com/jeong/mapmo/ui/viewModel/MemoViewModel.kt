package com.jeong.mapmo.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.data.common.MapmoApplication
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.db.AppDatabase
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.entities.MemoEntity
import com.jeong.mapmo.data.repository.MemoRepository
import com.jeong.mapmo.data.repository.MemoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MemoViewModel : ViewModel() {

    val repository = MemoRepositoryImpl()


    private var _memoList: MutableStateFlow<MemoResult<List<Memo>>> =
        MutableStateFlow(MemoResult.Loading)
    val memoList: StateFlow<MemoResult<List<Memo>>> = _memoList.asStateFlow()


    fun updateMemo(memo: MemoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMemo(memo)
    }


    fun deleteMemo(memo: MemoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMemo(memo)
    }

    fun getMemo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMemo()
                .catch {
                    _memoList.emit(MemoResult.RoomDBError(it))
                }
                .collectLatest {
                    if (it.isNotEmpty()) {
                        _memoList.emit(
                            MemoResult.Success(
                                it.map {
                                    Memo(
                                        title = it.title,
                                        longitude = it.longitude,
                                        latitude = it.latitude,
                                        detail = it.detail,
                                        priority = it.priority,
                                        checked = it.checked
                                    )
                                })
                        )
                    } else _memoList.emit(MemoResult.NoConstructor)
//                when (it) {
//                    is MemoResult.Loading -> {
//                        Log.d("taaag", "MemoResult.Loading")
//                    }
//
//                    is MemoResult.Success -> {
//                        _memoList.value =
//                            it.resultData.map {
//                                Memo(
//                                    title = it.title,
//                                    longitude = it.longitude,
//                                    latitude = it.latitude,
//                                    detail = it.detail,
//                                    priority = it.priority,
//                                    checked = it.checked
//                                )
//                            }.toMutableList()
//                        Log.d("taaag", "MemoResult.Success")
//                    }
//
//                    is MemoResult.RoomDBError -> {
//                        Log.e("RoomDBError", it.toString())
//                        Log.d("taaag", "에러")
//                    }
//
//                    else -> {
//                        Log.d("taaag", "else로 빠짐")
//                    }
//                }
                }

        }
    }

}
