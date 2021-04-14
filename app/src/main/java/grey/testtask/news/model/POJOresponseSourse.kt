package grey.testtask.news.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class POJOresponseSourse {
    @SerializedName("status")
    @Expose
    var status: String = ""

    @SerializedName("sources")
    @Expose
    var sources: List<Source> = emptyList()
}