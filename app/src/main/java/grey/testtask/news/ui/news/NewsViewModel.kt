package grey.testtask.news.ui.news

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import grey.testtask.news.App
import grey.testtask.news.R
import grey.testtask.news.model.Article
import grey.testtask.news.network.NewsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class NewsViewModel(private val application: App) : AndroidViewModel(application) {

    private var _newsList = MutableLiveData<List<Article>>(emptyList<Article>().toMutableList())
    val newsList: LiveData<List<Article>>
        get() = _newsList

    private var _isProgress = MutableLiveData<Boolean>(false)
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    private var sources = ""
    private var nextPageNumber = 1

    fun getNewsFromSources(sources: String) {
        this.sources = sources
        viewModelScope.launch {
            _newsList.value = application.database.articleDao().getNews()
            if (sources == "")
                Toast.makeText(
                    application,
                    application.getString(R.string.add_to_favorites),
                    Toast.LENGTH_LONG
                ).show()
            else {
                if (checkConnection())
                    loadNews(sources)
            }
        }
    }

    fun getNextPage() {
        if (checkConnection()) {
            viewModelScope.launch {
                loadNews(sources)
            }
        }
    }

    private suspend fun loadNews(sources: String) {
        newsList.value?.let {
            if (it.isEmpty())
                _isProgress.value = true
        }
        try {
            NewsLoader.getEverithing(sources, "", nextPageNumber)?.let {
                _isProgress.value = false
                val list = _newsList.value?.toMutableList()
                if (nextPageNumber == 1)
                    list?.addAll(0, it.articles ?: emptyList())
                else
                    list?.addAll(it.articles ?: emptyList())
                if ((it.totalResults % (App.RESULTS_ON_PAGE * nextPageNumber)) > nextPageNumber)
                    nextPageNumber++
                val set = list?.toSet()
                _newsList.value = set?.toList()
                application.database.articleDao().insertNewsList(_newsList.value)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            var toastTtext = ""
            _isProgress.value = false
            e.response()?.errorBody()?.let {
                val errorBody = withContext(Dispatchers.IO) {
                    JSONObject(it.string())
                }
                toastTtext = errorBody.getString("code")
            } ?: let {
                toastTtext = e.message.toString()
            }
            Toast.makeText(application, toastTtext, Toast.LENGTH_LONG)
                .show()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            _isProgress.value == false
            Toast.makeText(application, e.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkConnection(): Boolean {
        val cm =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected) {
            true
        } else {
            Toast.makeText(
                application,
                application.resources.getString(R.string.network_not_available),
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
}