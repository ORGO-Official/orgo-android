package com.orgo.core.model.data.mountain

data class FeatureTagLists(
    val enableList : List<FeatureType>,
    val unableList : List<FeatureType>,
    val totalCourse : Int = 0
)