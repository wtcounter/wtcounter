package wordtextcounter.details.main.feature.settings

import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings_flow.tvRateUs
import kotlinx.android.synthetic.main.fragment_settings_flow.tvShareApp
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.notes.NotesViewModel
import wordtextcounter.details.main.feature.notes.NotesViewModelFactory
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.Constants.PLAY_STORE_URL
import wordtextcounter.details.main.util.RateUsHelper.redirectToPlayStore
import wordtextcounter.details.main.util.launchActionSendIntent

class SettingsFlowFragment : BaseFragment() {

  private lateinit var viewModelFactory: NotesViewModelFactory

  //TODO Replace this with Setting's ViewModel
  private lateinit var viewModel: NotesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = NotesViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao()
    )
    viewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(NotesViewModel::class.java)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_settings_flow, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    addChildFragment(SettingsFragment.newInstance(), R.id.settings_container)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val psDrawable = AppCompatResources.getDrawable(context!!, R.drawable.ic_google_play)
    tvRateUs.setCompoundDrawablesWithIntrinsicBounds(psDrawable, null, null, null)
    tvRateUs.setOnClickListener {
      AnalyticsLogger.logAnalytics(Click("settings_rate_app"))
      this@SettingsFlowFragment.activity?.let { activity ->
        redirectToPlayStore(activity)
      }
    }

    val shareDrawable = AppCompatResources.getDrawable(context!!, R.drawable.ic_share_black_24dp)
    tvShareApp.setCompoundDrawablesWithIntrinsicBounds(shareDrawable, null, null, null)
    tvShareApp.setOnClickListener {
      AnalyticsLogger.logAnalytics(Click("settings_share_app"))
      val shareText = getString(R.string.share_app_text) + "\n" + PLAY_STORE_URL
      context?.let { context ->
        try {
          launchActionSendIntent(
              context, shareText, getString(R.string.share_picker_text)
          )
        } catch (e: ActivityNotFoundException) {
          showError(getString(R.string.activity_not_found_error))
        }
      }
    }
  }

  //TODO Replace this with Setting's ViewModel
  override val baseViewModel: BaseViewModel
    get() = viewModel

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFlowFragment.
     */
    fun newInstance(): SettingsFlowFragment {
      val fragment = SettingsFlowFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}