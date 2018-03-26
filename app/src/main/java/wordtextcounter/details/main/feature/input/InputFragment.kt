package wordtextcounter.details.main.feature.input

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_input.etInput
import kotlinx.android.synthetic.main.fragment_input.toolbar
import kotlinx.android.synthetic.main.report_folded.tvCharacters
import kotlinx.android.synthetic.main.report_folded.tvSentences
import kotlinx.android.synthetic.main.report_folded.tvWords
import kotlinx.android.synthetic.main.report_summary.foldingCell
import kotlinx.android.synthetic.main.report_summary.ibSave
import kotlinx.android.synthetic.main.report_summary.ivExpand
import kotlinx.android.synthetic.main.report_unfolded.tvCharactersContent
import kotlinx.android.synthetic.main.report_unfolded.tvParagraphsContent
import kotlinx.android.synthetic.main.report_unfolded.tvReportText
import kotlinx.android.synthetic.main.report_unfolded.tvSentencesContent
import kotlinx.android.synthetic.main.report_unfolded.tvSizeContent
import kotlinx.android.synthetic.main.report_unfolded.tvWordsContent
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {

  private lateinit var viewModel: InputViewModel

  private val TEXT = "TEXT"

  private var avMoreToLess: AnimatedVectorDrawableCompat? = null
  private var avLessToMore: AnimatedVectorDrawableCompat? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(InputViewModel::class.java)
    avMoreToLess = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_more_to_less)
    avLessToMore = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_less_to_more)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_input, container, false)
  }

  @SuppressLint("ClickableViewAccessibility", "RxSubscribeOnError", "RxDefaultScheduler")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    ibSave.setOnClickListener {
      viewModel.onClickSaveCurrent()
    }

    foldingCell.initialize(1000, ContextCompat.getColor(context!!, R.color.folder_back_side), 0)
    ivExpand.setOnClickListener {

      if (foldingCell.isUnfolded) {
        ivExpand.setImageDrawable(avLessToMore)
        avLessToMore?.start()
      } else {
        ivExpand.setImageDrawable(avMoreToLess)
        avMoreToLess?.start()
      }

      foldingCell.toggle(false)
    }
    etInput.setText(savedInstanceState?.getString(TEXT))

    disposable.add(RxTextView
        .textChanges(etInput)
        .debounce(300, TimeUnit.MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          viewModel.onClickConfirm(it.toString())
        })

    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(TEXT, etInput.text.toString())
  }

  private fun handleViewState(viewState: ViewState) {

    if (viewState.showError) {
      showError(viewState.errorMessage)
    }

    ivExpand.visibility = if (viewState.showExpand) VISIBLE else GONE

    if (!viewState.showExpand && foldingCell.isUnfolded) {
      ivExpand.setImageDrawable(avLessToMore)
      avLessToMore?.start()
      foldingCell.fold(false)
    }

    tvCharacters.text = viewState.noOfCharacters
    tvWords.text = viewState.noOfWords
    tvSentences.text = viewState.noOfSentences


    tvCharactersContent.text = viewState.noOfCharacters
    tvWordsContent.text = viewState.noOfWords
    tvSentencesContent.text = viewState.noOfSentences
    tvReportText.text = viewState.reportText
    tvParagraphsContent.text = viewState.noOfParagraphs
    tvSizeContent.text = viewState.size
  }


  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputFragment.
     */
    fun newInstance(): InputFragment {
      return InputFragment()
    }
  }
}// Required empty public constructor
