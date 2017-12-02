package wordtextcounter.details.main.feature.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import wordtextcounter.details.main.R

/**
 * Created by hirak on 02/12/17.
 */
abstract class BaseActivity : AppCompatActivity() {

  private var unbinder: Unbinder? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayout())
    unbinder = ButterKnife.bind(this)
  }

  override fun onDestroy() {
    unbinder?.unbind()
    super.onDestroy()
  }

  @LayoutRes protected abstract
  fun getLayout(): Int

  fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
  }
}