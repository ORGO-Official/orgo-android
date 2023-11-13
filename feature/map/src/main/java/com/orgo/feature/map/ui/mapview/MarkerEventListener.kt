package com.orgo.feature.map.ui.mapview

import com.orgo.core.model.data.mountain.Mountain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import timber.log.Timber

class MarkerEventListener : MapView.POIItemEventListener {
    companion object{
        val instance = MarkerEventListener()
    }
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _marker = MutableStateFlow(-1)
    val marker = _marker.asStateFlow()

    fun onPOIItemUnselected() {
        scope.launch { _marker.emit(-1) }
    }
    // 마커 클릭
    override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
        Timber.d("마커 클릭됨")
        val poiMountainId = (poiItem?.userObject as Mountain).id ?: -1
        mapView?.setMapCenterPoint(poiItem.mapPoint,true)
        scope.launch { _marker.emit(poiMountainId)}
    }
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?){} // 말풍선 클릭
    @Deprecated("Deprecated in Java")
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}     // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시킨 경우
}