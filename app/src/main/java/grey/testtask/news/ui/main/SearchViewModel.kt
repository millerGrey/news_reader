package grey.testtask.news.ui.main

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

class SearchViewModel(private val application: App) : AndroidViewModel(application) {

    private var _newsList = MutableLiveData<List<Article>>(emptyList<Article>().toMutableList())
    val newsList: LiveData<List<Article>>
        get() = _newsList

    private var _isProgress = MutableLiveData<Boolean>(false)
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    private var nextPageNumber = 1
    private var keyword = ""
    private var lastKeyword = ""

    fun clearList() {
        _newsList.value = emptyList()
        nextPageNumber = 1
    }

    fun getNewsByKeyword(keyword: String) {
        this.keyword = keyword
        if(lastKeyword != keyword){
            clearList()
        }
        lastKeyword = keyword
        viewModelScope.launch {
            if (checkConnection()) {
                newsList.value?.let {
                    if (it.isEmpty())
                        _isProgress.value = true
                }
                try {
                    NewsLoader.getEverithing("", keyword, nextPageNumber)?.let {
                        _isProgress.value = false
                        if(it.totalResults == 0){
                            Toast.makeText(application, application.resources.getString(R.string.no_results), Toast.LENGTH_LONG)
                                .show()
                        }
                        if ((it.totalResults % (App.RESULTS_ON_PAGE * nextPageNumber)) > nextPageNumber)
                            nextPageNumber++
                        val list = _newsList.value?.toMutableList()
                        list?.addAll(it.articles ?: emptyList())
                        _newsList.value = list ?: emptyList()
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                    var toastTtext = ""
                    _isProgress.value == false
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
        }
    }

    fun getNextPage() {
        getNewsByKeyword(keyword)
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