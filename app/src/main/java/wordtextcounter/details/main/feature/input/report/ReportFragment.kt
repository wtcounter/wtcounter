package wordtextcounter.details.main.feature.input.report

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_report.rvResult
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.input.InputViewModel.ReportState
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : BaseFragment() {

  private val compositeDisposable = CompositeDisposable()


  private var resultAdapter = ReportAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_report, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rvResult.adapter = resultAdapter
    compositeDisposable.add(
        RxBus.instance.subscribe(ReportState::class.java, Consumer { setResult(it) }))
  }

  private fun setResult(reportState: ReportState) {
    Log.d("set result ", reportState.toString())
    resultAdapter.setResults(reportState.list)
  }


  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReportFragment.
     */
    fun newInstance(): ReportFragment {
      val fragment = ReportFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}// Required empty public constructor
