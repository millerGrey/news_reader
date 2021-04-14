package grey.testtask.news.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "sourceTable")
data class Source(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    @PrimaryKey
    @NotNull
    var name: String = "",

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("category")
    @Expose
    var category: String? = null,

    @SerializedName("language")
    @Expose
    var language: String? = null,

    @SerializedName("country")
    @Expose
    var country: String = ""
) {
    var isFavorite: Boolean = false
}