package wordtextcounter.details.main.feature.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by hirak on 02/12/17.
 */
abstract class BaseFragment : Fragment() {

  private var unbinder: Unbinder? = null
  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    unbinder = view?.let { ButterKnife.bind(this, it) }
  }

  override fun onDestroyView() {
    unbinder?.unbind()
    super.onDestroyView()
  }
}