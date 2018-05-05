package wordtextcounter.details.main.feature.base

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
import wordtextcounter.details.main.R

abstract class BaseActivity : AppCompatActivity() {

  fun replaceFragment(fragment: Fragment) {

    val backStackName = fragment.javaClass.name

    val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStackName, 0)

    Logger.d("Fragment popped $fragmentPopped")

    if (!fragmentPopped && supportFragmentManager.findFragmentByTag(backStackName) == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, fragment)
          .addToBackStack(backStackName)
          .commit()
    }
  }
}