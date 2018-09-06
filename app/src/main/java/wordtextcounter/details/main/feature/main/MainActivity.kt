package wordtextcounter.details.main.feature.main

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.ViewGroup
import com.example.rateus.RateusCore
import com.roughike.bottombar.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.bottombar
import kotlinx.android.synthetic.main.activity_main.container
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.feature.settings.SettingsFlowFragment
import wordtextcounter.details.main.util.dpToPx
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click

class MainActivity : BaseActivity(), OnTabSelectListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    bottombar.setOnTabSelectListener(this, savedInstanceState == null)

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

        val llp = container.layoutParams as CoordinatorLayout.LayoutParams
        llp.setMargins(0, 0, 0, 0)
        container.layoutParams = llp
      } else {
        bottombar.shySettings.showBar()
        val llp = container.layoutParams as CoordinatorLayout.LayoutParams
        llp.setMargins(0, 0, 0, resources.getDimensionPixelOffset(R.dimen._48sdp))
        container.layoutParams = llp
      }
    }

    bottombar.setTabSelectionInterceptor { _, _ ->
      RateusCore.showRateUsDialog(this)
      return@setTabSelectionInterceptor false
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

  override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    bottombar.removeOnTabSelectListener()
    super.onRestoreInstanceState(savedInstanceState)
    bottombar.setOnTabSelectListener(this, false)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    bottombar.onSaveInstanceState()
    super.onSaveInstanceState(outState)
  }

  override fun onBackPressed() {
    RateusCore.showRateUsDialog(this)
    if (supportFragmentManager.backStackEntryCount == 1) {
      finish()
    } else {
      super.onBackPressed()
    }
  }

  override fun onTabSelected(tabId: Int) {
    when (tabId) {
      R.id.tab_input -> {
        logAnalytics(Click("bottombar_input"))
        replaceFragment(InputFragment.newInstance())
      }
      R.id.tab_notes -> {
        logAnalytics(Click("bottombar_notes"))
        replaceFragment(NotesFragment.newInstance())
      }
      R.id.tab_settings -> {
        logAnalytics(Click("bottombar_settings"))
        replaceFragment(SettingsFlowFragment.newInstance())
      }
    }
  }
}
