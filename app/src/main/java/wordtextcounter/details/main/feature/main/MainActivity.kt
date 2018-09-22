package wordtextcounter.details.main.feature.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.ViewGroup
import com.example.rateus.RateusCore.shouldShowRateUsDialog
import com.orhanobut.logger.Logger
import com.roughike.bottombar.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.bottombar
import kotlinx.android.synthetic.main.activity_main.container
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsConsent.showConsentDialog
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.feature.settings.SettingsFlowFragment
import wordtextcounter.details.main.util.RxBus
import wordtextcounter.details.main.util.dpToPx
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.feature.notes.NotesMainFragment
import wordtextcounter.details.main.util.Constants
import wordtextcounter.details.main.util.NewText
import wordtextcounter.details.main.util.RateUsHelper.showRateUsDialog
import wordtextcounter.details.main.util.extensions.getPreference

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
      showRateUsDialog(this)
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

    val pf = getPreference()
    val consent = pf.getBoolean(Constants.PREF_ANALYTICS_CONSENT, false)
    if (!consent) {
      showConsentDialog(this, pf)
    }
  }

  private fun handleNewIntent() {
    if (intent?.action == Intent.ACTION_SEND) {
      if ("text/plain" == intent.type) {
        Logger.d("text/plain")
        handleSendText(intent) // Handle text being sent
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleNewIntent()
  }

  private fun handleSendText(intent: Intent) {
    Logger.d("handleSendText")
    intent.getStringExtra(Intent.EXTRA_TEXT)
        ?.let {
          Logger.d("Sending new text event")
          RxBus.send(NewText(it))
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
    // if count is more than 1, then we take user directly to InputFragment regardless of what backstack is.
    if (supportFragmentManager.backStackEntryCount > 1) {
      replaceFragment(InputFragment.newInstance())

    } else {
      // while leaving, check if we can show Rate us dialog.
      if (shouldShowRateUsDialog(this)) {
        showRateUsDialog(this)
      } else {
        saveCurrentTextIfRequired()
        finish()
      }
    }
  }

  private fun saveCurrentTextIfRequired() {
    val currentFragment = getCurrentFragment()
    if (currentFragment is InputFragment) {
      currentFragment.saveCurrentTextToPreference()
    }
  }

  override fun onStop() {
    super.onStop()
    val currentFragment = getCurrentFragment()
    if (currentFragment is InputFragment) {
      currentFragment.saveDraft()
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
        replaceFragment(NotesMainFragment.newInstance())
      }
      R.id.tab_settings -> {
        logAnalytics(Click("bottombar_settings"))
        replaceFragment(SettingsFlowFragment.newInstance())
      }
    }
  }
}
