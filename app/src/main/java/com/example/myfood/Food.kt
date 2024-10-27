package com.example.myfood

import android.content.ClipDescription
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food (
    val name: String,
    val description: String,
    var img: Int,
    val dataBahan: String,
    val dataIntruksi: String,
    val photoTopMemasak: Int,
): Parcelable