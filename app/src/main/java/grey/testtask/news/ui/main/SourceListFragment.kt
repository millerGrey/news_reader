package grey.testtask.news.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import grey.testtask.news.App
import grey.testtask.news.NewsAdapter
import grey.testtask.news.R
import grey.testtask.news.ViewModelFactory
import grey.testtask.news.databinding.FragmentMainBinding
import grey.testtask.news.ui.news.NewsActivity
import grey.testtask.news.ui.news.NewsFragment

class SourceListFragment : Fragment() {

    private val sourceListViewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory(App.instance))
            .get(SourceListViewModel::class.java)
    }
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        index = arguments?.getInt(ARG_SECTION_NUMBER) ?: CHANNELS_LIST_FRAGMENT_INDEX
        if (index == CHANNELS_LIST_FRAGMENT_INDEX) {
            sourceListViewModel.getSourcesList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val binding = FragmentMainBinding.bind(view)
        with(binding) {
            viewModel = sourceListViewModel
            fragmentIndex = index
            sourceListViewModel.channels.value?.let {
                recyclerView.adapter =
                    NewsAdapter(it, sourceListViewModel::onClick, null)
            }
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            lifecycleOwner = requireActivity()
            return root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (index == FAVORITE_LIST_FRAGMENT_INDEX)
            inflater.inflate(R.menu.menu_favorites_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.showNews) {
            val intent = Intent(context, NewsActivity::class.java)
            val sources =
                sourceListViewModel.channels.value?.filter { it.isFavorite }?.map { it.id }
                    ?.joinToString(",")
            intent.putExtra(NewsFragment.ARG_SOURCES, sources)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        const val CHANNELS_LIST_FRAGMENT_INDEX = 1
        const val FAVORITE_LIST_FRAGMENT_INDEX = 2

        @JvmStatic
        fun newInstance(sectionNumber: Int): SourceListFragment {
            return SourceListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}