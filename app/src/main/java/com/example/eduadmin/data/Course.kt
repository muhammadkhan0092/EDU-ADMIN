package com.example.eduadmin.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  Course(
    val courseId: String = "",
    val uid : String = "",
    val title : String = "",
    val category: String = "",
    val image: String = "",
    val rating: Double = 0.0,
    val sold:Int = 0,
    val price : String="",
    val description : String="",
    val videos : MutableList<VideoData> = mutableListOf(),
    val students : MutableList<String>  = mutableListOf(),
    val paid : Int = 0
) : Parcelable
