package wordtextcounter.details.main.feature.main

import android.os.Bundle
import com.roughike.bottombar.OnTabSelectListener
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.bottombar
import kotlinx.android.synthetic.main.activity_main.toolbar
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.util.RxBus

class MainActivity : BaseActivity(), OnTabSelectListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    RxBus.instance.subscribe(ToolbarTitle::class.java,
        Consumer {
          toolbar.setTitle(it.title)
        })

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
}
