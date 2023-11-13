package com.orgo.core.model.data

import java.lang.Math.round
import kotlin.math.roundToInt

data class Restaurant(
    val name: String,
    val address: String,
    val distance: Double,
    val mapX: Double,
    val mapY: Double,
    val contact: String,
    val imageUrl: String,
    val externalLink: String,
){
    val formattedName : String
        get() = name.replace(" ", "\n")
    val distanceInKilometers : Double
        get() = (distance / 1000.0 * 10.0).roundToInt() / 10.0
    companion object{
        val DUMMY_RESTAURANT = Restaurant(
            "찜집 본점",
            "서울특별시 중랑구 면목로 287",
            1764.3561632864155,
            127.0859581133,
            37.5785103427,
            "",
            "http://tong.visitkorea.or.kr/cms/resource/07/2851807_image2_1.jpg",
            "http://place.map.kakao.com/9970114"
        )
        val DUMMY_RESTAURANT2 = Restaurant(
            "이름이 생각보다 긴 레스토랑",
            "서울특별시 중랑구 면목로 287",
            1764.3561632864155,
            127.0859581133,
            37.5785103427,
            "",
            "http://tong.visitkorea.or.kr/cms/resource/07/2851807_image2_1.jpg",
            "http://place.map.kakao.com/9970114"
        )
    }
}