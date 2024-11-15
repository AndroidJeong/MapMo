package com.jeong.mapmo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.entities.MemoEntity
import com.jeong.mapmo.data.repository.MemoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemoViewModel : ViewModel() {

    val repository = MemoRepositoryImpl()
    private var _memoList: MutableStateFlow<MemoResult<List<Memo>>> =
        MutableStateFlow(MemoResult.Loading)
    val memoList: StateFlow<MemoResult<List<Memo>>> = _memoList.asStateFlow()


    fun updateMemo(checked: Boolean, title: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateMemo(checked, title)
    }

    fun deleteMemo(title: String) = viewModelScope.launch(Dispatchers.IO) {
        //질문 하나의 데이터(_memoList)를 가지고 동작해야할거 같은데
//        if (title.isNotEmpty()){
//            _memoList.emit(
//                MemoResult.Success(
//                    MemoEntity
//                )
//            )
//        }

        repository.deleteMemo(title)
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
                                        locationName = it.locationName,
                                        priority = it.priority,
                                        checked = it.checked
                                    )
                                })
                        )
                    } else _memoList.emit(MemoResult.NoConstructor)
                }

        }
    }
}
