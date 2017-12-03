package wordtextcounter.details.main.feature.main

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.roughike.bottombar.BottomBar
import io.reactivex.functions.Consumer
import wordtextcounter.details.main.BuildConfig
import wordtextcounter.details.main.R
import wordtextcounter.details.main.R.layout
import wordtextcounter.details.main.feature.base.BaseActivity
import wordtextcounter.details.main.feature.input.InputFragment
import wordtextcounter.details.main.feature.notes.NotesFragment
import wordtextcounter.details.main.util.RxBus


class MainActivity : BaseActivity() {

  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.bottombar) lateinit var bottombar: BottomBar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val formatStrategy = PrettyFormatStrategy.newBuilder()
        .methodCount(5)         // (Optional) How many method line to show. Default 2
        .tag(
            BuildConfig.APPLICATION_ID)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
        .build()

    Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return BuildConfig.DEBUG
      }
    })
    setSupportActionBar(toolbar)

    bottombar.setOnTabSelectListener { tabId ->
      when (tabId) {
        R.id.tab_input -> replaceFragment(InputFragment.newInstance())
        R.id.tab_notes -> replaceFragment(NotesFragment.newInstance("", ""))
      }
    }

    RxBus.instance.subscribe(ToolbarTitle::class.java,
        Consumer {
          toolbar.setTitle(it.title)
        })

  }

  override fun getLayout() = layout.activity_main


  class ToolbarTitle(@StringRes var title: Int)
}
