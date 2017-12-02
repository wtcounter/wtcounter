package wordtextcounter.details.main.feature.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife
import wordtextcounter.details.main.R

/**
 * Created by hirak on 02/12/17.
 */
abstract class BaseActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d("onCreate", BaseActivity::class.toString())
    setContentView(getLayout())
    ButterKnife.bind(this)
  }

  override fun onDestroy() {
    Log.d("onDestroy", BaseActivity::class.toString())
    super.onDestroy()
  }

  @LayoutRes protected abstract
  fun getLayout(): Int

  fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
  }
}