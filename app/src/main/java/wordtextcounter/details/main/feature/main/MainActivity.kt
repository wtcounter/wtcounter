package wordtextcounter.details.main.feature.main

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View.GONE
import android.view.View.VISIBLE
import com.roughike.bottombar.OnTabSelectListener
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.bottombar
import kotlinx.android.synthetic.main.activity_main.clMainActivity
import kotlinx.android.synthetic.main.activity_main.toolbar
import wordtextcounter.details.main.R
import wordtextcounter.details.main.R.layout
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.util.RxBus
import wordtextcounter.details.main.util.dpToPx

class MainActivity : BaseActivity(), OnTabSelectListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setSupportActionBar(toolbar)

        RxBus.instance.subscribe(ToolbarTitle::class.java,
                Consumer {
                    if (it.showBottom) {
                        bottombar.visibility = VISIBLE
                    } else {
                        bottombar.visibility = GONE
                    }
                    if (it.showToolbar) {
                        toolbar.visibility = VISIBLE
                        toolbar.setTitle(it.title)
                    } else {
                        toolbar.visibility = GONE
                    }

                })


        clMainActivity.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff =
                    clMainActivity.rootView.height - clMainActivity.height
            if (heightDiff > dpToPx(this@MainActivity,
                            200F)) { // if more than 200 dp, it's probably a keyboard...
                bottombar.visibility = GONE
            } else {
                bottombar.visibility = VISIBLE
            }
        }
        bottombar.setOnTabSelectListener(this)

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
            bottombar.removeOnTabSelectListener()
            if (currentFragment is InputFragment) {
                bottombar.selectTabWithId(R.id.tab_input)
            } else if (currentFragment is NotesFragment) {
                bottombar.selectTabWithId(R.id.tab_notes)
            }
            bottombar.setOnTabSelectListener(this, false)
        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onTabSelected(tabId: Int) {
        when (tabId) {
            R.id.tab_input -> replaceFragment(InputFragment.newInstance())
            R.id.tab_notes -> replaceFragment(NotesFragment.newInstance())
        }
    }

    override fun getLayout() = layout.activity_main

    class ToolbarTitle(@StringRes val title: Int, val showBottom: Boolean = true,
            val showToolbar: Boolean = true)
}
