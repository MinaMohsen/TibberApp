package com.example.tibberapp.domain.repository

import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface PowerUpsRepository {

    suspend fun getPowerUps () : Flow<Resource<List<AssignmentData>>>
}