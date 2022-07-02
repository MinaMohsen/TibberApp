package com.example.tibberapp.data.repository

import com.example.tibberapp.domain.repository.PowerUpsRepository
import com.example.tibberapp.data.datasource.remote.RemoteDataSource
import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PowerUpsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : PowerUpsRepository {

    override suspend fun getPowerUps(): Flow<Resource<List<AssignmentData>>> {
        return flow { emit(remoteDataSource.getPowerUps()) }
    }

}