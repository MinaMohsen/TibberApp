package com.example.tibberapp.data.datasource.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.example.tibberapp.data.mappers.mapToDomainModel
import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.util.Constants.NO_INTERNET_CONNECTION_ERROR_CODE
import com.example.tibberapp.util.Resource
import com.example.tipperapp.GetPowerUpsQuery
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : RemoteDataSource {

    override suspend fun getPowerUps(): Resource<List<AssignmentData>> {
        return try {
            val result = apolloClient.query(GetPowerUpsQuery()).await()
            if (result.hasErrors()) {
                Resource.Error(result.errors?.first()?.message)
            } else {
                val list = mutableListOf<AssignmentData>()
                result.data?.assignmentData()?.forEach { it ->
                    list.add(it.mapToDomainModel())
                }
                list.sortByDescending { it.connected }
                Resource.Success(list)
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message, errorCode = NO_INTERNET_CONNECTION_ERROR_CODE)
        }
    }
}
