package com.example.tibberapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssignmentData(
    val title: String,
    val description: String,
    val longDescription: String,
    val connected: Boolean,
    val storeUrl: String,
    val imageUrl: String
) : Parcelable {
    constructor() : this("", "", "", false, "", "")
}