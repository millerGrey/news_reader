package grey.testtask.news.network

import grey.testtask.news.App
import grey.testtask.news.model.POJOresponseEverything
import grey.testtask.news.model.POJOresponseSourse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {
    @Headers("X-Api-Key: 867b52893ccf4dd0b6f4594f62311bf2")
    @GET("/v2/everything?pageSize=${App.RESULTS_ON_PAGE}")
    suspend fun getEverything(
        @Query("sources") sources: String,
        @Query("q") keyword: String,
        @Query("page") page: Int
    ): POJOresponseEverything


    @Headers("X-Api-Key: 867b52893ccf4dd0b6f4594f62311bf2")
    @GET("/v2/sources")
    suspend fun getSources(): POJOresponseSourse
}