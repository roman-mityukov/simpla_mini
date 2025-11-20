package io.mityukov.simpla.network.retrofit

import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.GET

@Serializable
enum class RemoteIntervalType {
    Walk, Run
}

@Serializable
data class RemoteTraining(val id: String, val intervals: List<RemoteInterval>)

@Serializable
data class RemoteInterval(val id: String, val intervalType: RemoteIntervalType, val duration: Int)

interface RemoteApi {
    @GET("b/CINVD")
    fun getTraining(): Call<RemoteTraining>
}