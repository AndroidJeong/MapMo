package com.jeong.mapmo.data.common

sealed class MemoResult<out T> {
    data object NoConstructor : MemoResult<Nothing>()
    data object Loading : MemoResult<Nothing>()
    data class Success<T>(val resultData: T) : MemoResult<T>()
    data class RoomDBError(val exception: Throwable) : MemoResult<Nothing>()

}