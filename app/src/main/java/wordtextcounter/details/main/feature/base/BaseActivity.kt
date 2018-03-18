package wordtextcounter.details.main.feature.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import wordtextcounter.details.main.R

abstract class BaseActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayout())
  }

  @LayoutRes
  protected abstract
  fun getLayout(): Int

  fun replaceFragment(fragment: Fragment) {

    val backStackName = fragment.javaClass.name

    val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStackName, 0)

    if (!fragmentPopped && supportFragmentManager.findFragmentByTag(backStackName) == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, fragment)
          .addToBackStack(backStackName)
          .commit()
    }
  }
}