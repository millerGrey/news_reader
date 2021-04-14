package grey.testtask.news

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter<T>(
    var list: List<T>,
    private val onItemClick: ((String) -> Unit)?,
    private val getNextPage: (() -> Unit)?
) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Any?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolderFactory.createHolder(parent, list[0], onItemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        getNextPage?.let {
            if (position == list.size - 1) {
                it.invoke()
            }
        }
    }

    fun refreshList(list: List<T>) {
        this.list = list
        notifyDataSetChanged()
    }
}
