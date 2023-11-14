package com.orgo.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.orgo.core.designsystem.R
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object OrgoIcon{
    val Home = R.drawable.home_icon
    val HomeBorder = R.drawable.home_boder_icon
    val Badge = R.drawable.badge
    val BadgeBorder = R.drawable.badge_border
    val Mypage = R.drawable.mypage_icon
    val MypageBorder = R.drawable.mypage_border_icon


    val MyLocation = R.drawable.mylocation_icon
    val CurrentLocationMarker = R.drawable.current_location_marker_icon
    val Mountain1 = R.drawable.mountain_icon_1
    val Mountain2 = R.drawable.mountain_icon_2
    val Mountain3 = R.drawable.mountain_icon_3
    val Mountain4 = R.drawable.mountain_icon_4
    val NoResult = R.drawable.no_result
    val Location = R.drawable.location
    val Setting = R.drawable.setting
    val Search = R.drawable.search_icon
    val ArrowBack = R.drawable.back_icon
    val Refresh = R.drawable.refresh_icon

    val Course = R.drawable.course_icon
    val NightView = R.drawable.nightview_icon
    val Parking = R.drawable.parking_icon
    val Restroom = R.drawable.toilet_icon
    val CableCar = R.drawable.cablecar_icon

    val Difficulty_Border = R.drawable.difficulty_icon
    val Difficulty_Filled = R.drawable.filled_difficulty_icon
    val DragHandle = R.drawable.drag_handle

    val Today_Orgo = R.drawable.today_orgo
    val MountainLocation = R.drawable.mountain_location_icon
    val Orgo_WaterMark = R.drawable.orgo_watermark
    val SaveImage = R.drawable.save_image_icon
    val CompleteCheck = R.drawable.complete_check_icon
    val Camera = R.drawable.camera_icon
    val Gallery = R.drawable.gallery_icon
    val KakaoTalk = R.drawable.kakaotalk_logo
    val Instagram = R.drawable.instagram_logo

    //Badges
    val Dummy = R.drawable.dummy_badge
    val EarlyBird = R.drawable.earlybird_badge
    val FiveHundredMeters = R.drawable.five_hundred_m_badge
    val OneKm = R.drawable.one_km_badge
    val ThreeKm = R.drawable.three_km_badge
    val Jan = R.drawable.month_1_badge
    val Feb = R.drawable.month_2_badge
    val Mar = R.drawable.month_3_badge
    val Apr = R.drawable.month_4_badge
    val May = R.drawable.month_5_badge
    val Jun = R.drawable.month_6_badge
    val Jul = R.drawable.month_7_badge
    val Aug = R.drawable.month_8_badge
    val Sep = R.drawable.month_9_badge
    val Oct = R.drawable.month_10_badge
    val Nov = R.drawable.month_11_badge
    val Dec = R.drawable.month_12_badge

    //아차산
    val Acha_Bronze = R.drawable.acha_bronze_badge
    val Acha_Silver = R.drawable.acha_silver_badge
    val Acha_Gold = R.drawable.acha_gold_badge
    // 인왕산
    val Inwang_Bronze = R.drawable.inwang_bronze_badge
    val Inwang_Silver = R.drawable.inwang_silver_badge
    val Inwang_Gold = R.drawable.inwang_gold_badge
    // 수락산
    val Surak_Bronze = R.drawable.surak_bronze_badge
    val Surak_Silver = R.drawable.surak_silver_badge
    val Surak_Gold = R.drawable.surak_gold_badge
    // 불암산
    val Buram_Bronze = R.drawable.buram_bronze_badge
    val Buram_Silver = R.drawable.buram_silver_badge
    val Buram_Gold = R.drawable.buram_gold_badge
    // 용마산
    val Yongma_Bronze = R.drawable.yongma_bronze_badge
    val Yongma_Silver = R.drawable.yongma_silver_badge
    val Yongma_Gold = R.drawable.yongma_gold_badge
    //관악산
    val Gwanak_Bronze = R.drawable.gwanak_bronze_badge
    val Gwanak_Silver = R.drawable.gwanak_silver_badge
    val Gwanak_Gold = R.drawable.gwanak_gold_badge
    //도봉산
    val Dobong_Bronze = R.drawable.dobong_bronze_badge
    val Dobong_Silver = R.drawable.dobong_silver_badge
    val Dobong_Gold = R.drawable.dobong_gold_badge
    // 청계산
    val Cheonggye_Bronze = R.drawable.cheonggye_bronze_badge
    val Cheonggye_Silver = R.drawable.cheonggye_silver_badge
    val Cheonggye_Gold = R.drawable.cheonggye_gold_badge
    // 안산
    val Unak_Bronze = R.drawable.unak_bronze_badge
    val Unak_Silver = R.drawable.unak_silver_badge
    val Unak_Gold = R.drawable.unak_gold_badge
    //북한산
    val Bukhan_Bronze = R.drawable.bukhan_bronze_badge
    val Bukhan_Silver = R.drawable.bukhan_silver_badge
    val Bukhan_Gold = R.drawable.bukhan_gold_badge

}

sealed class IconType{
    data class ImageVectorIcon(val imageVector: ImageVector) : IconType()
    data class DrawableResourceIcon(@DrawableRes val id : Int) : IconType()
}


@Composable
fun Icon(
    iconType: IconType,
    contentDescription : String? = null,
    modifier : Modifier = Modifier,
    tint : Color = LocalContentColor.current
) {
    when (iconType) {
        is IconType.ImageVectorIcon -> Icon(
            imageVector = iconType.imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )
        is IconType.DrawableResourceIcon -> Icon(
            painter = painterResource(id = iconType.id),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )
    }
}
