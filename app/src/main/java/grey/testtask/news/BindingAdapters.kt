package grey.testtask.news

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import grey.testtask.news.model.Article
import grey.testtask.news.model.Source
import grey.testtask.news.ui.main.SourceListFragment

object BindingAdapters {

    @BindingAdapter("app:items", "app:index")
    @JvmStatic
    fun setItems(listView: RecyclerView, items: List<Source>, index: Int) {
        var list: List<Source> = emptyList()
        if (index == SourceListFragment.CHANNELS_LIST_FRAGMENT_INDEX) {
            list = items
        } else if (index == SourceListFragment.FAVORITE_LIST_FRAGMENT_INDEX) {
            list = items.filter { it.isFavorite }
        }
        (listView.adapter as NewsAdapter<Source>).refreshList(list)
    }

    @BindingAdapter("app:favorite")
    @JvmStatic
    fun setItems(view: View, isFavorite: Boolean) {
        when (isFavorite) {
            true -> view.setBackgroundResource(R.drawable.ic_baseline_star_24)
            false -> view.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
        }
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(listView: RecyclerView, items: List<Article>) {
        (listView.adapter as NewsAdapter<Article>).refreshList(items)
    }
}
