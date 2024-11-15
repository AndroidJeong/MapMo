package com.jeong.mapmo.data.remote

data class KakaoSearchResponse(
    val documents: List<KakaoPlace>
)

data class KakaoPlace(
    val place_name: String,
    val road_address_name: String,
    val x: String,
    val y: String
)