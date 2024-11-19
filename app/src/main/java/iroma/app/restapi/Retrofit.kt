package iroma.app.restapi

import android.telecom.Call
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.opendota.com/api/"

object OkHttp {
    val client = OkHttpClient().newBuilder()
        .followRedirects(true)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followSslRedirects(false)
        .build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(OkHttp.client)
    .build()

object CyberScoreApi {
    val retrofitService: OpenDotaApiService by lazy {
        retrofit.create(OpenDotaApiService::class.java)
    }
}

interface OpenDotaApiService {
    @GET("proMatches")
    suspend fun loadMatches(
        @Query("less_than_match_id") lessThanMatchId: Long?
    ): List<OpenDotaMatchSimple>
    @GET("matches/{id}")
    suspend fun getMatch(
        @Path("id") id: Long
    ): OpenDotaMatchDetails

    @GET("leagues")
    suspend fun loadLeagues(): List<OpenDotaLeagueRaw>

    @GET("heroes")
    suspend fun loadHeroes(): List<OpenDotaHero>
}
@Serializable
data class OpenDotaLeagueRaw(
    val name: String? = null,
    val leagueid: Long? = null,
    val tier: String? = null,
)
@Serializable
class OpenDotaMatchDetails(
    val match_id: Long?,
    val error: String?,
    val radiant_win: Boolean?,
    val leagueid: Long?,
    var duration: Int?,
    var radiant_score: Long?,
    var dire_score: Long?,
    var players: List<OpenDotaPlayer>?,
)
@Serializable
class OpenDotaPlayer(
    var account_id: Long? = null,
    var hero_id: Int? = null,
    var team_number: Int? = null,
)
@Serializable
class OpenDotaMatchSimple(
    val match_id: Long,
    val radiant_name: String? = null,
    val dire_name: String? = null,
    val leagueid: Long? = null,
    var radiant_team_id: Long? = null,
    var dire_team_id: Long? = null,
)
@Serializable
class OpenDotaHero(
    val id: Int,
    val name: String,//npc_dota_hero_mars
    val localized_name: String,//Mars
    val attack_type: String
)