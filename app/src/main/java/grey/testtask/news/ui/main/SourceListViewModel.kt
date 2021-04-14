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
import grey.testtask.news.model.Source
import grey.testtask.news.network.NewsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException

class SourceListViewModel(private val application: App) : AndroidViewModel(application) {

    private var _channels = MutableLiveData<List<Source>>(emptyList())
    val channels: LiveData<List<Source>>
        get() = _channels

    private var _isProgress = MutableLiveData<Boolean>(false)
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    fun getSourcesList() {
        viewModelScope.launch {
            _channels.value = application.database.sourceDao().getSources()
            if (_channels.value?.isEmpty()!!) {
                _isProgress.value = true
                if (checkConnection()) {
                    try {
                        NewsLoader.getSources()?.let {
                            _isProgress.value = false
                            _channels.value = it.sources
                            application.database.sourceDao().insertSourceList(it.sources)
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
    }

    fun onClick(name: String) {
        val list = _channels.value ?: emptyList()
        var toastMessage = ""
        list.find { it.name == name }?.let { item ->
            item.isFavorite = !item.isFavorite
            if (item.isFavorite) {
                toastMessage = application.getString(R.string.added_to_favorites)
            } else {
                toastMessage = application.getString(R.string.removed_from_favorites)
            }
            _channels.value = list
            viewModelScope.launch {
                application.database.sourceDao().updateSource(item)
                Toast.makeText(application, String.format(toastMessage, name), Toast.LENGTH_SHORT)
                    .show()
            }
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