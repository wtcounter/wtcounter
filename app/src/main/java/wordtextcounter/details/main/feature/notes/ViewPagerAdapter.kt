package wordtextcounter.details.main.feature.notes

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

class ViewPagerAdapter(private val fragments : List<Fragment>,
                       private val titles : List<String>,
                       private val fragmentManager : FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager) {

  override fun getItem(position: Int) = fragments[position]

  override fun getCount() = fragments.size

  override fun getPageTitle(position: Int): CharSequence? {
    return titles[position]
  }
}