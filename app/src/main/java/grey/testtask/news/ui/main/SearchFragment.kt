package grey.testtask.news.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import grey.testtask.news.App
import grey.testtask.news.NewsAdapter
import grey.testtask.news.R
import grey.testtask.news.ViewModelFactory
import grey.testtask.news.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.activity_main.*

class SearchFragment : Fragment() {

    private var searchText: EditText? = null
    private var isSearchTextVisible: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory(App.instance)
        ).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchText = requireActivity().editTextSearch
        searchText?.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == IME_ACTION_DONE) {
                viewModel.getNewsByKeyword(searchText?.text.toString())
            }
            false
        }
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val binding = FragmentSearchBinding.bind(view)
        with(binding) {
            searchViewModel = viewModel
            viewModel.newsList.value?.let {
                newsRecyclerView.adapter =
                    NewsAdapter(it, null, viewModel::getNextPage)
            }
            newsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            lifecycleOwner = requireActivity()
            return root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search_fragment, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.searchNews)
        if (searchText?.visibility == View.GONE) {
            item.setIcon(R.drawable.ic_baseline_search_24)
        } else {
            item.setIcon(R.drawable.ic_baseline_close_24)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchNews) {
            searchText?.let {
                if (it.visibility == View.VISIBLE) {
                    it.setText("")
                } else {
                    item.setIcon(R.drawable.ic_baseline_close_24)
                    it.visibility = View.VISIBLE
                    it.requestFocus()
                    (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (!menuVisible && isAdded) {
            searchText?.visibility = View.GONE
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(
                    searchText?.applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
        }
    }

    override fun onResume() {
        searchText?.visibility = isSearchTextVisible
        if (isSearchTextVisible == View.GONE)
            viewModel.clearList()
        super.onResume()
    }

    fun onBackPressed(): Boolean {
        searchText?.let {
            if ((it.visibility == View.VISIBLE)) {
                it.setText("")
                it.visibility = View.GONE
                requireActivity().invalidateOptionsMenu()
                return true
            }
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        searchText?.let {
            outState.putString(KEY_SEARCH_TEXT, it.text.toString())
            outState.putInt(KEY_SEARCH_TEXT_VISIBILITY, it.visibility)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        searchText?.setText(savedInstanceState?.getString(KEY_SEARCH_TEXT))
        isSearchTextVisible = savedInstanceState?.getInt(KEY_SEARCH_TEXT_VISIBILITY) ?: View.GONE
    }

    companion object {
        private const val KEY_SEARCH_TEXT = "searchText"
        private const val KEY_SEARCH_TEXT_VISIBILITY = "searchTextIsVisible"
    }
}