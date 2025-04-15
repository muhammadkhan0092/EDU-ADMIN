package com.example.eduadmin.data

import android.os.Parcelable
import com.example.eduadmin.data.TimeData
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoData(
    val videoId : String = "",
    val videoLength : String = "",
    val videoThumbnail : String = "",
    val title : String = "",
    val timeData: TimeData = TimeData(0,0),
    var watchedData : TimeData = TimeData(0,0)
) : Parcelable
