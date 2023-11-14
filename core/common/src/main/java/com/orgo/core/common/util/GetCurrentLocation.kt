package com.orgo.core.common.util

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Parcel
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.orgo.core.model.data.mountain.Mountain

fun getCurrentLocation(context : Context) : Location?{
    val permissionCheck = ContextCompat.checkSelfPermission(context,ACCESS_FINE_LOCATION)
    return if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    } else {
        Toast.makeText(context,"정확한 위치정보 사용에 동의하여야합니다.",Toast.LENGTH_SHORT).show()
        null
    }
}

fun getTestLocation(selectedMountain : Mountain) : Location{
    val location = Location("")
    location.latitude = selectedMountain.location.latitude
    location.longitude = selectedMountain.location.longitude
    location.altitude = selectedMountain.location.altitude
    return location

}