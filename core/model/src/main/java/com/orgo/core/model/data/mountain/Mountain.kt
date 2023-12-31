package com.orgo.core.model.data.mountain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mountain(
    val id: Int,
    val name: String,
    val description: String,
    val address: String,
    val contact: String,
    val mainImage : String,
    val backgroundImage : String,
    val requiredTime : String,
    val difficulty: String,
    val location: OrgoLocation,
    val featureTag: FeatureTag,
):Parcelable{
    companion object{
        val DUMMY_MOUNTAIN = Mountain(
            id = 1,
            name = "아차산",
            description = "너른 벌판 위를 달리던 한 줄기 바람이 갑작스럽게 숨을 몰아쉬어야 하는 곳, 우뚝 아차(蛾嵯)라고 이름 한 곳이 바로 아차산이다. 정상표고 200m 되는 지점에서 시작하여 동남의 한강 변 쪽으로 경사진 산허리의 윗부분을 둘러싸고 있는 산성의 형태가 남아 있다. 백제의 도읍이 한강 유역에 있을 때 우뚝 솟은 지형적 특성으로 인해 일찍부터 이 아차산에 흙을 깎고 다시 돌과 흙으로 쌓아 올려 산성을 축조함으로써 고구려의 남하를 막으려는 백제인의 노력이 있었다. 한강을 사이에 두고 맞은 편 남쪽에 있는 풍납동 토성과 함께 중요한 군사적 요지로서 백제의 운명을 좌우하던 곳이기도 하다. 아단성(阿旦城), 아차성(蛾嵯城), 장하성, 광장성 등으로 불리기도 하여 백제, 신라, 고구려가 한강을 중심으로 공방전을 장기간에 걸쳐 벌였던 것을 짐작할 수 있다. 이 성의 흔적은 60년대까지만 해도 눈으로 알아볼 수 있을 정도로 남아있었다. 아차산 분수령의 전부와 그 북쪽 기슭 면목동의 동쪽과 아차산의 서남쪽 기슭을 달리는 진맥의 분수령 및 그곳부터는 분명치 못하지만, 모진동 밭에 이르는 사이에 이어져 있었던 길이 4km에 달하는 토성과 석 성 자리는 신라가 쌓은 장한성으로 알려졌다. 1,500여 년이라는 장구한 역사의 수레바퀴 속에서 여러 차례 그 운명해야 했던 아차산성은 아직도 그 자신의 운명을 나타내려는 듯 당시의 토기와 기왓조각 등을 보여주고 있으며, 그 옛날 산성 수비군의 역할을 다시 되새겨보려는 듯 많은 사람이 오르는 시민공원이 되어 그 역사적 변신을 꾀하고 있다.",
            address = "서울특별시 광진구 중곡동",
            contact = "02-450-1114",
            mainImage = "https://san.chosun.com/news/photo/202302/22412_82258_3814.jpg",
            backgroundImage = "https://dl.dongascience.com/uploads/article/Contents/199305/S199305N007_img_01.jpg",
            requiredTime = "50분 ~ 2시간 이내",
            difficulty = "NORMAL",
            location = OrgoLocation(37.57149,127.103764,287.0),
            featureTag = FeatureTag(false,false,false,false,1)
        )
    }
}


