package wordtextcounter.details.main.feature.input

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_input.etInput
import kotlinx.android.synthetic.main.fragment_input.fabSave
import kotlinx.android.synthetic.main.fragment_input.toolbar
import kotlinx.android.synthetic.main.report_folded.tvCharacters
import kotlinx.android.synthetic.main.report_folded.tvSentences
import kotlinx.android.synthetic.main.report_folded.tvWords
import kotlinx.android.synthetic.main.report_summary.foldingCell
import kotlinx.android.synthetic.main.report_summary.ivExpand
import kotlinx.android.synthetic.main.report_unfolded.tvCharactersContent
import kotlinx.android.synthetic.main.report_unfolded.tvParagraphsContent
import kotlinx.android.synthetic.main.report_unfolded.tvReportText
import kotlinx.android.synthetic.main.report_unfolded.tvSentencesContent
import kotlinx.android.synthetic.main.report_unfolded.tvSizeContent
import kotlinx.android.synthetic.main.report_unfolded.tvWordsContent
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.RxBus
import java.util.concurrent.TimeUnit.MILLISECONDS


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
  private lateinit var viewModelFactory: InputViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModelFactory = InputViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao())
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(InputViewModel::class.java)
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

    val cView = LayoutInflater.from(activity).inflate(R.layout.report_name_edit, null)
    val rName = cView.findViewById<AppCompatEditText>(R.id.rName)
    fabSave.setOnClickListener {
      MaterialStyledDialog.Builder(activity)
          .setTitle("") // This is intentional. Not providing this results into weird UI.
          .setDescription(getString(R.string.save_dialog_desc))
          .withDarkerOverlay(true)
          .setPositiveText(getString(R.string.bookmark))
          .setNegativeText(getString(R.string.cancel))
          .setCustomView(cView, 20, 20, 20, 20)
          .onPositive { _, _ ->
            if (rName.text.trim().isEmpty()) {
              //TODO Error message
            }
            viewModel.onClickSaveCurrent(rName.text.toString())
          }
          .onNegative { dialog, _ ->
            dialog.dismiss()
          }
          .setIcon(R.drawable.note_add)
          .show()
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
//    etInput.setText(savedInstanceState?.getString(TEXT))


    disposable.add(RxTextView
        .textChanges(etInput)
        .debounce(300, MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          viewModel.calculateInput(it.toString())
        })




    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

  }

  override fun onStart() {
    super.onStart()
//    etInput.setText("Hello world")

    disposable.add(RxBus.subscribe(EditReport::class.java, Consumer {
      etInput.setText(it.report.dataText)
    }))

  }


  private fun handleViewState(viewState: ViewState) {

    if (viewState.showError) {
      showError(viewState.errorMessage)
    }

    ivExpand.visibility = if (viewState.showExpand) VISIBLE else GONE


    if (viewState.showExpand) {
      fabSave.show()
    } else {
      fabSave.hide()
    }

    if (!viewState.showExpand && foldingCell.isUnfolded) {
      ivExpand.setImageDrawable(avLessToMore)
      avLessToMore?.start()
      foldingCell.fold(false)
    }

    tvCharacters.text = viewState.report?.characters
    tvWords.text = viewState.report?.words
    tvSentences.text = viewState.report?.sentences


    tvCharactersContent.text = viewState.report?.characters
    tvWordsContent.text = viewState.report?.words
    tvSentencesContent.text = viewState.report?.sentences
    tvReportText.text = viewState.report?.dataText
    tvParagraphsContent.text = viewState.report?.paragraphs
    tvSizeContent.text = viewState.report?.size
  }

  override val baseViewModel: BaseViewModel
    get() = viewModel


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
