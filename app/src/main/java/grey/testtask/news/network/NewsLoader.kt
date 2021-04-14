package grey.testtask.news.network

import grey.testtask.news.model.POJOresponseEverything
import grey.testtask.news.model.POJOresponseSourse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsLoader {

    private const val NEWSAPI_URL = "https://newsapi.org"

    private var retrofit = Retrofit.Builder()
        .baseUrl(NEWSAPI_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var newsAPI = retrofit.create(NewsApi::class.java)


    suspend fun getEverithing(
        sources: String,
        keyword: String,
        page: Int
    ): POJOresponseEverything? = newsAPI.getEverything(sources, keyword, page)

    suspend fun getSources(): POJOresponseSourse? = newsAPI.getSources()
}
