package grey.testtask.news.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articleTable", primaryKeys = ["title", "publishedAt"])
data class Article(

    @SerializedName("source")
    @Expose
    @Embedded
    var source: Source? = null,

    @SerializedName("author")
    @Expose
    var author: String? = null,

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "articleDescriprtion")
    var description: String? = null,

    @SerializedName("urlToImage")
    @Expose
    var urlToImage: String? = null,

    @SerializedName("publishedAt")
    @Expose
    var publishedAt: String = ""
) {
    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("url")
    @Expose
    @ColumnInfo(name = "articleUrl")
    var url: String? = null
}