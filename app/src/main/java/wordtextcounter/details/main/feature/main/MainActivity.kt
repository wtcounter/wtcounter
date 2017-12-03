package wordtextcounter.details.main.feature.main

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import butterknife.BindView
import io.reactivex.functions.Consumer
import wordtextcounter.details.main.R.id
import wordtextcounter.details.main.R.layout
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.util.RxBus

class MainActivity : BaseActivity() {

  @BindView(id.toolbar) lateinit var toolbar: Toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(toolbar)

    if (savedInstanceState == null)
      replaceFragment(InputFragment.newInstance())

    RxBus.instance.subscribe(ToolbarTitle::class.java,
        Consumer {
          toolbar.setTitle(it.title)
        })

  }

  override fun getLayout() = layout.activity_main


  class ToolbarTitle(@StringRes var title: Int)
}
