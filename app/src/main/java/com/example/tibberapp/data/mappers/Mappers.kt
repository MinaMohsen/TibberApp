package com.example.tibberapp.data.mappers

import com.example.tibberapp.domain.model.AssignmentData
import com.example.tipperapp.GetPowerUpsQuery

fun GetPowerUpsQuery.AssignmentDatum.mapToDomainModel() = AssignmentData(
    this.title(),
    this.description(),
    this.longDescription(),
    this.connected(),
    this.storeUrl(),
    this.imageUrl()
)