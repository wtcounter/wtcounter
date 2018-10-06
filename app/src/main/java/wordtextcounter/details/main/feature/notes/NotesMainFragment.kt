package wordtextcounter.details.main.feature.notes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_notes_main.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Event
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics

class NotesMainFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_notes_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val fragments = mutableListOf<Fragment>()
    fragments.add(NotesFragment.newInstance())
    fragments.add(DraftsFragment.newInstance())
    val titles = mutableListOf<String>()
    titles.add(getString(R.string.notes))
    titles.add(getString(R.string.drafts))
    viewPager.adapter = ViewPagerAdapter(fragments, titles, childFragmentManager)
    tabLayout.setupWithViewPager(viewPager)

    viewPager.addOnPageChangeListener(object : OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {
        //no-op
      }

      override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
      ) {
        //no-op
      }

      override fun onPageSelected(position: Int) {
        if (position == 0) {
          logAnalytics(Event("tab_notes_selected"))
        } else if (position == 1) {
          logAnalytics(Event("tab_drafts_selected"))
        }
      }
    })
  }

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotesMainFragment.
     */
    fun newInstance(): NotesMainFragment {
      val fragment = NotesMainFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}