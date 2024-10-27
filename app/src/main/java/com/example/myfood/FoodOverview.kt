package com.example.myfood

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodOverview(
    val dataPhoto: Int,
) : Parcelable

@Parcelize
data class FoodMemasak(
    val dataPhotoMemasak: Int
): Parcelable
