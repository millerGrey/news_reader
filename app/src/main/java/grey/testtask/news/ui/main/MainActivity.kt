package grey.testtask.news.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import grey.testtask.news.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        val searchFragment =
            supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 2)
        searchFragment?.let {
            if ((searchFragment is SearchFragment) && searchFragment.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}