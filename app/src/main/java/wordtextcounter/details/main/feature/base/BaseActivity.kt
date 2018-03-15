package wordtextcounter.details.main.feature.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.orhanobut.logger.Logger
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

    Logger.d("Fragment to replace $fragment")

    Logger.d("Current fragment before replacing " + supportFragmentManager.findFragmentById(
        R.id.container))

    val backStackName = fragment.javaClass.name

    val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStackName, 0)

    Logger.d("fragment popped $fragmentPopped")
    if (!fragmentPopped) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, fragment)
          .addToBackStack(backStackName)
          .commit()
    }
  }
}