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
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, fragment)
        .addToBackStack(null)
        .commit()
  }
}