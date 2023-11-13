package com.orgo.core.model.data.mountain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeatureTag(
    val cableCar: Boolean,
    val goodNightView: Boolean,
    val parkingLot: Boolean,
    val restRoom: Boolean,
    val totalCourse: Int
):Parcelable

fun FeatureTag.toFeatureTagLists(): FeatureTagLists {
    val enableList = mutableListOf<FeatureType>()
    val unableList = mutableListOf<FeatureType>()

    if (totalCourse > 0) enableList.add(FeatureType.TOTAL_COURSE) else unableList.add(FeatureType.TOTAL_COURSE)
    if (goodNightView) enableList.add(FeatureType.GOOD_NIGHT_VIEW) else unableList.add(FeatureType.GOOD_NIGHT_VIEW)
    if (restRoom) enableList.add(FeatureType.REST_ROOM) else unableList.add(FeatureType.REST_ROOM)
    if (parkingLot) enableList.add(FeatureType.PARKING_LOT) else unableList.add(FeatureType.PARKING_LOT)
    if (cableCar) enableList.add(FeatureType.CABLE_CAR) else unableList.add(FeatureType.CABLE_CAR)

    return FeatureTagLists(enableList.toList(), unableList.toList(), totalCourse)
}
