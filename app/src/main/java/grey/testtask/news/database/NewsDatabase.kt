package grey.testtask.news.database

import androidx.room.Database
import androidx.room.RoomDatabase
import grey.testtask.news.model.Article
import grey.testtask.news.model.Source

@Database(entities = [Article::class, Source::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDAO
    abstract fun sourceDao(): SourceDAO

}
