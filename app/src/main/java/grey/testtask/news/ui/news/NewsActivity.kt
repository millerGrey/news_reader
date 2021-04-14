package grey.testtask.news.ui.news


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import grey.testtask.news.R

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = intent.getStringExtra(NewsFragment.ARG_SOURCES)
        setContentView(R.layout.activity_news)
        str?.let { NewsFragment.newInstance(it) }?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_placeholder, it, "news_fragment")
                .commit()
        }
    }
}