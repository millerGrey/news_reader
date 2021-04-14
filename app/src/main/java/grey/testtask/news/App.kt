package grey.testtask.news

import android.app.Application
import androidx.room.Room
import grey.testtask.news.database.NewsDatabase

class App : Application() {
    lateinit var database: NewsDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, NewsDatabase::class.java, DB_NAME)
            .build()
    }

    companion object {
        private const val DB_NAME = "database.db"
        const val RESULTS_ON_PAGE = 20
        lateinit var instance: App
    }
}