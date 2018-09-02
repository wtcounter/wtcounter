package wordtextcounter.details.main.feature.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.rateus.RateusCore
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_notes.progressBar
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.input.InputFragment

abstract class BaseFragment : Fragment() {

  abstract val baseViewModel: BaseViewModel

  val disposable = CompositeDisposable()

  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    retainInstance = true

    baseViewModel.routerState.observe(this, Observer {
      when (it) {
        is Input -> (activity as BaseActivity).replaceFragment(InputFragment.newInstance())
      }
    })

    baseViewModel.loaderState.observe(this, Observer {
      it?.let {
        if (it) {
          progressBar.visibility = View.VISIBLE
        } else {
          progressBar.visibility = View.GONE
        }
      }
    })
    this@BaseFragment.activity?.parent?.let { it1 -> RateusCore.showRateUsDialog(it1) }
  }

  fun addChildFragment(fragment: Fragment, @IdRes id: Int) {
    childFragmentManager
        .beginTransaction()
        .replace(id, fragment)
        .commit()
  }

  override fun onDestroyView() {
    disposable.clear()
    super.onDestroyView()
  }

  protected fun showError(message: String) {
    view?.let {
      Snackbar.make(it, message, Snackbar.LENGTH_LONG)
          .show()
    }
  }
}