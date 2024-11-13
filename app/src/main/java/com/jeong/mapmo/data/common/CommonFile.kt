package com.jeong.mapmo.data.common

import android.widget.Toast

//질문 파일위치, 파일명?
fun toastCommon(message: String){
    Toast.makeText(MapmoApplication.getApplication(), message, Toast.LENGTH_SHORT).show()
}