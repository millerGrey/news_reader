package grey.testtask.news.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import grey.testtask.news.App
import grey.testtask.news.NewsAdapter
import grey.testtask.news.R
import grey.testtask.news.ViewModelFactory
import grey.testtask.news.databinding.FragmentNewsBinding


class NewsFragment : Fragment() {

    private var sources = ""
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory(App.instance)
        ).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sources = arguments?.getString(ARG_SOURCES) ?: ""
        viewModel.getNewsFromSources(sources)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val binding = FragmentNewsBinding.bind(view)
        with(binding) {
            newsViewModel = viewModel
            viewModel.newsList.value?.let {
                newsRecyclerView.adapter = NewsAdapter(it, null, viewModel::getNextPage)
            }
            newsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            lifecycleOwner = requireActivity()
            return root
        }
    }

    companion object {
        const val ARG_SOURCES = "sources"
        fun newInstance(sources: String): NewsFragment {
            val args = Bundle()
            args.putString(ARG_SOURCES, sources)
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

