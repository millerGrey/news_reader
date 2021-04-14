package grey.testtask.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import grey.testtask.news.ui.main.SearchViewModel
import grey.testtask.news.ui.main.SourceListViewModel
import grey.testtask.news.ui.news.NewsViewModel

class ViewModelFactory(
    private val application: App
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        NewsViewModel::class.java -> NewsViewModel(application) as T

        SourceListViewModel::class.java -> SourceListViewModel(application) as T

        SearchViewModel::class.java -> SearchViewModel(application) as T

        else -> throw RuntimeException("Cannot create an instance of $modelClass")
    }
}