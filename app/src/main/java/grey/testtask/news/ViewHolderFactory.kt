package grey.testtask.news

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.squareup.picasso.Picasso
import grey.testtask.news.databinding.ListItemNewsBinding
import grey.testtask.news.databinding.ListItemSourceBinding
import grey.testtask.news.model.Article
import grey.testtask.news.model.Source

object ViewHolderFactory {

    class NewsViewHolder(v: View) : NewsAdapter.ViewHolder(v) {

        private var displayWidth = 0
        private var displayHeight = 0
        private val binding = ListItemNewsBinding.bind(v)
        private val imageView = binding.imageView!!
        private val wm = imageView.context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager

        override fun bind(item: Any?) {
            if (item is Article) {
                binding.news = item
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    displayWidth = wm.currentWindowMetrics.bounds.width()
                    displayHeight = wm.currentWindowMetrics.bounds.height()
                } else {
                    displayWidth = wm.defaultDisplay.width
                    displayHeight = wm.defaultDisplay.height
                }
                if (displayHeight > displayWidth) {
                    imageView.layoutParams.height = displayHeight / ITEM_ON_PAGE
                    imageView.layoutParams.width = displayWidth
                    imageView.requestLayout()
                }
                Picasso.get()
                    .load(item.urlToImage)
                    .transform(object : CropImageTransformation(
                        imageView.layoutParams.width,
                        imageView.layoutParams.height
                    ) {})
                    .placeholder(R.drawable.ic_baseline_library_books_24)
                    .into(imageView)
                binding.executePendingBindings()
            }
        }

        companion object {
            private const val ITEM_ON_PAGE = 3
        }
    }

    class ChannelViewHolder(v: View, private val onItemClick: ((String) -> Unit)?) :
        NewsAdapter.ViewHolder(v) {
        private val binding = ListItemSourceBinding.bind(v)
        override fun bind(item: Any?) {
            if (item is Source) {
                binding.channel = item
                binding.clickListener = View.OnClickListener {
                    val name = item.name
                    onItemClick?.let { onClick -> onClick(name) }
                }
                binding.executePendingBindings()
            }
        }
    }


    fun createHolder(
        parent: ViewGroup,
        zeroItem: Any?,
        onItemClick: ((String) -> Unit)?
    ): NewsAdapter.ViewHolder {
        return when (zeroItem) {
            is Article -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_news, parent, false)
                NewsViewHolder(v)
            }
            is Source -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_source, parent, false)
                ChannelViewHolder(v, onItemClick)
            }
            else -> throw IllegalStateException("Add viewholder implementation for this item: ${zeroItem?.javaClass ?: "null"}")
        }
    }
}