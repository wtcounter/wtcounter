package wordtextcounter.details.main

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import butterknife.BindView
import io.reactivex.functions.Consumer
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.util.RxBus

class MainActivity : BaseActivity() {

  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)
    replaceFragment(InputFragment.newInstance())

    RxBus.instance.subscribe(ToolbarTitle::class.java,
        Consumer {
          toolbar.setTitle(it.title)
        })

  }

  override fun getLayout() = R.layout.activity_main


  class ToolbarTitle(@StringRes var title: Int)
}
