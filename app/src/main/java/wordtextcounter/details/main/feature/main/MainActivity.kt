package wordtextcounter.details.main.feature.main

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.roughike.bottombar.OnTabSelectListener
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.bottombar
import kotlinx.android.synthetic.main.activity_main.container
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.util.RxBus
import wordtextcounter.details.main.util.dpToPx

class MainActivity : BaseActivity(), OnTabSelectListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)
    Logger.d("onCreate $savedInstanceState")

    if (savedInstanceState != null) {
      bottombar.onRestoreInstanceState(savedInstanceState)
      bottombar.setOnTabSelectListener(this, false)
    } else {
      bottombar.setOnTabSelectListener(this)
    }



    RxBus.instance.subscribe(ToolbarTitle::class.java,
        Consumer {
          Logger.d("Subscribe $it")
//                    toolbar.setTitle(it.title)
        })

    val activityRootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
      val heightDiff =
        activityRootView.rootView.height - activityRootView.height
      if (heightDiff > dpToPx(
              this@MainActivity,
              200F
          )
      ) { // if more than 200 dp, it's probably a keyboard...
        bottombar.shySettings.hideBar()

        val llp = container.layoutParams as ConstraintLayout.LayoutParams
        llp.setMargins(0, 0, 0, 0)
        container.layoutParams = llp
      } else {
        bottombar.shySettings.showBar()
        val llp = container.layoutParams as ConstraintLayout.LayoutParams
        llp.setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen._48sdp))
        container.layoutParams = llp
      }
    }


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

  override fun onSaveInstanceState(outState: Bundle?) {
    bottombar.onSaveInstanceState()
    Logger.d("onSaveInstanceState")
    super.onSaveInstanceState(outState)
  }

  override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount == 1) {
      finish()
    } else {
      super.onBackPressed()
    }
  }

  override fun onTabSelected(tabId: Int) {
    Logger.d("onTabSelected $tabId")
    when (tabId) {
      R.id.tab_input -> replaceFragment(InputFragment.newInstance())
      R.id.tab_notes -> replaceFragment(NotesFragment.newInstance())
    }
  }
}
