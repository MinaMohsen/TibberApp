package com.example.tibberapp.data.datasource.remote

import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.util.Resource

interface RemoteDataSource {
    suspend fun getPowerUps(): Resource<List<AssignmentData>>
}