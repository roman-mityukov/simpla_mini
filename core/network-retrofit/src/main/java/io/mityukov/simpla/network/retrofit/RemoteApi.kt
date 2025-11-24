package io.mityukov.simpla.network.retrofit

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
enum class RemoteIntervalType {
    Walk, Run
}

@Serializable
data class RemoteTraining(
    @SerialName("timer_id")
    val id: Int,
    val title: String,
    @SerialName("total_time")
    val totalTime: Int,
    val intervals: List<RemoteInterval>,
)

@Serializable
data class RemoteTimer(val timer: RemoteTraining)

@Serializable
data class RemoteInterval(val title: String, val time: Int)

interface RemoteApi {
    @GET("{id}")
    fun getTraining(@Path("id") id: String): Call<RemoteTimer>
}