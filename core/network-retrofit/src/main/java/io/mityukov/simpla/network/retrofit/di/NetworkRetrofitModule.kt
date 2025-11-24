package io.mityukov.simpla.network.retrofit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mityukov.simpla.log.logd
import io.mityukov.simpla.network.retrofit.RemoteApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkRetrofitModule {
    companion object {
        @Provides
        fun providesRemoteApi(okHttpClient: OkHttpClient): RemoteApi {
            val contentType = "application/json".toMediaType()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl("https://sr111.05.testing.place/api/v2/interval-timers/")
                    .client(okHttpClient)
                    .addConverterFactory(Json.asConverterFactory(contentType))
                    .build()
            return retrofit.create(RemoteApi::class.java)
        }

        @Provides
        fun providesOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor { message -> logd(message) }
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor {
                    val token = "Bearer pdhO16atBIXogpPzaLDjDcl5Gpmbz9Mdl1mjhrhWZBuOgNCgxDlk7mMIbFcEc7mj"

                    val newRequest = it.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build()
                    it.proceed(newRequest)
                }
                .addInterceptor(loggingInterceptor)
                .build()
            return okHttpClient
        }
    }
}