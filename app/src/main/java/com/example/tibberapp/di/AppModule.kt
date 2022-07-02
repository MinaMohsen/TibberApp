package com.example.tibberapp.di

import com.apollographql.apollo.ApolloClient
import com.example.tibberapp.data.datasource.remote.RemoteDataSource
import com.example.tibberapp.data.datasource.remote.RemoteDataSourceImpl
import com.example.tibberapp.data.repository.PowerUpsRepositoryImpl
import com.example.tibberapp.domain.repository.PowerUpsRepository
import com.example.tibberapp.domain.usecase.GetPowerUpsUseCase
import com.example.tibberapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetPowerUpsUseCase(powerUpsRepository: PowerUpsRepository): GetPowerUpsUseCase {
        return GetPowerUpsUseCase(powerUpsRepository)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apolloClient: ApolloClient): RemoteDataSource {
        return RemoteDataSourceImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun providePowerUpsRepository(
        remoteDataSource: RemoteDataSource
    ): PowerUpsRepository {
        return PowerUpsRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient =
        ApolloClient.builder().serverUrl(Constants.BASE_URL)
            .okHttpClient(
                OkHttpClient
                    .Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val builder = original.newBuilder().method(
                            original.method(),
                            original.body()
                        )
                        chain.proceed(builder.build())
                    }.build()
            )
            .build()

}