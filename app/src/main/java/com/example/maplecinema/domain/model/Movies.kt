import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    val status: String = "",
    val msg: String = "",
    val data: Data = Data()
)

@Serializable
data class Data(
    val seoOnPage: SEOOnPage = SEOOnPage(),
    val breadCrumb: List<BreadCrumb> = emptyList(),
    val titlePage: String = "",
    val items: List<Item> = emptyList(),
    val params: Params = Params(),

    @SerialName("type_list")
    val typeList: String = "",

    @SerialName("APP_DOMAIN_FRONTEND")
    val appDomainFrontend: String = "",

    @SerialName("APP_DOMAIN_CDN_IMAGE")
    val appDomainCDNImage: String = ""
)

@Serializable
data class BreadCrumb(
    val name: String = "",
    val slug: String? = null,
    val isCurrent: Boolean = false,
    val position: Long = 0
)

@Serializable
data class Item(
    val modified: Modified = Modified(),

    @SerialName("_id")
    val id: String = "",

    val name: String = "",
    val slug: String = "",

    @SerialName("origin_name")
    val originName: String = "",

    val type: String,

    @SerialName("poster_url")
    val posterURL: String = "",

    @SerialName("thumb_url")
    val thumbURL: String = "",

    @SerialName("sub_docquyen")
    val subDocquyen: Boolean = false,

    val chieurap: Boolean = false,
    val time: String = "",

    @SerialName("episode_current")
    val episodeCurrent: String = "",

    val quality: String,
    val lang: String,
    val year: Long = 0,
    val category: List<Category> = emptyList(),
    val country: List<Category> = emptyList()
)

@Serializable
data class Modified(
    val time: String = ""
)

@Serializable
data class Params(
    @SerialName("type_slug")
    val typeSlug: String = "",
    val filterCategory: List<String?> = emptyList(),
    val filterCountry: List<String?> = emptyList(),
    val filterYear: Long = 0,
    val filterType: String? = null,
    val sortField: String = "",
    val sortType: String = "",
    val pagination: Pagination = Pagination()
)

@Serializable
data class Pagination(
    val totalItems: Long = 0,
    val totalItemsPerPage: Long = 0,
    val currentPage: Long = 0,
    val totalPages: Long = 0
)

@Serializable
data class SEOOnPage(
    @SerialName("og_type")
    val ogType: String = "",

    val titleHead: String = "",
    val descriptionHead: String = "",

    @SerialName("og_image")
    val ogImage: List<String> = emptyList(),

    @SerialName("og_url")
    val ogURL: String = ""
)
