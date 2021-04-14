package grey.testtask.news.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class POJOresponseEverything {
    @SerializedName("status")
    @Expose
    var status: String = ""

    @SerializedName("totalResults")
    @Expose
    var totalResults: Int = 0

    @SerializedName("articles")
    @Expose
    var articles: List<Article>? = null
}