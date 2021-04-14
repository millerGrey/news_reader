package grey.testtask.news.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import grey.testtask.news.R

private val TAB_TITLES = arrayOf(
    R.string.channels,
    R.string.favorite,
    R.string.search
)


class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0, 1 -> SourceListFragment.newInstance(
                position + 1
            )
            2 -> SearchFragment()
            else -> throw IllegalStateException("Wrong fragment poition: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}