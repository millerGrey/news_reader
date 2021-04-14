package grey.testtask.news.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import grey.testtask.news.model.Article

@Dao
interface ArticleDAO {
    @Query("SELECT * from articleTable ORDER BY id DESC")
    suspend fun getNews(): List<Article>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(news: List<Article>?)
}