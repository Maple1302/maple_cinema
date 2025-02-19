import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieDetail(
    val status: Boolean,
    val msg: String,
    val movie: MovieOfDetailMovie,
    val episodes: List<Episode>
)
//) {
//    fun toJson() = Klaxon().toJsonString(this)
//
//    companion object {
//        fun fromJson(json: String) = Klaxon().parse<MovieDetail>(json)
//    }
//}

@Serializable
data class Episode(
    @SerialName("server_name") val serverName: String,
    @SerialName("server_data") val serverData: List<ServerData>
)

@Serializable
data class ServerData(
    val name: String,
    val slug: String,
    val filename: String,
    @SerialName("link_embed") val linkEmbed: String,
    @SerialName("link_m3u8") val linkM3U8: String
)

@Serializable
data class MovieOfDetailMovie(
    val tmdb: Tmdb? = null,
    val imdb: Imdb? = null,
    val created: Created? = null,
    val modified: Created? = null,
   @SerialName("_id") val id: String? = "",
    val name: String? = "",
    val slug: String? = "",
    @SerialName("origin_name") val originName: String? = "",
    val content: String? = "",
    val type: String? = "",
    val status: String? = "",
    @SerialName("poster_url") val posterUrl: String? = "",
    @SerialName("thumb_url") val thumbUrl: String? = "",
    @SerialName("is_copyright") val isCopyright: Boolean? = false,
    @SerialName("sub_docquyen") val subDocquyen: Boolean? = false,
    val chieurap: Boolean? = false,
    @SerialName("trailer_url") val trailerURL: String? = "",
    val time: String? = "",
    @SerialName("episode_current") val episodeCurrent: String? = "",
    @SerialName("episode_total") val episodeTotal: String? = "",
    val quality: String? = "",
    val lang: String? = "",
    val notify: String? = "",
    val showtimes: String? = "",
    val year: Long? = 2000,
    val view: Long? = 0,
    val actor: List<String>? = emptyList(),
    val director: List<String>? = emptyList(),
    val category: List<Category>? = emptyList(),
    val country: List<Category>? = emptyList()
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val slug: String
)

@Serializable
data class Created(
    val time: String
)

@Serializable
data class Imdb(
    val id: String? = null
)

@Serializable
data class Tmdb(
    val type: String?="",
    val id: String?="",
    val season: Long? = 0,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Long
)
