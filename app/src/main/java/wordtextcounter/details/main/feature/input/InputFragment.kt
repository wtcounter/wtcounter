package wordtextcounter.details.main.feature.input

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import com.jakewharton.rxbinding2.widget.RxTextView
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_input.etInput
import kotlinx.android.synthetic.main.fragment_input.fabSave
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
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.backToPosition
import wordtextcounter.details.main.util.onClick
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

  var cx: Int = -1
  var cy: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val dm = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(dm)

    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = resources.getDimensionPixelSize(resourceId)

    cx = dm.widthPixels / 2
    cy = dm.heightPixels / 2 - statusBarHeight

    viewModelFactory = InputViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao()
    )
    viewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(InputViewModel::class.java)
    avMoreToLess = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_more_to_less)
    avLessToMore = AnimatedVectorDrawableCompat.create(context!!, R.drawable.avd_less_to_more)

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_input, container, false)
  }

  @SuppressLint("ClickableViewAccessibility", "RxSubscribeOnError", "RxDefaultScheduler")
  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

//    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    fabSave onClick showDialog()

    foldingCell.initialize(500, ContextCompat.getColor(context!!, R.color.folder_back_side), 0)
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
        .debounce(300, MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          viewModel.onClickConfirm(it.toString())
        })

    viewModel.viewState.observe(this, Observer {
      it?.let { it1 -> handleViewState(it1) }
    })

  }

  private fun showDialog(): () -> Unit = {
    val cView = LayoutInflater.from(activity)
        .inflate(R.layout.report_name_edit, null)

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(cView)

    dialog.setOnShowListener {
      revealDialog(cView)
    }

    dialog.setOnKeyListener { dialog1, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_BACK) {
        hideDialog(cView, dialog1)
        return@setOnKeyListener true
      }
      false
    }

    dialog.setCanceledOnTouchOutside(false)
    dialog.window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
  }

  private fun hideDialog(
    cView: View,
    dialog: DialogInterface
  ) {
    val parentView = cView.findViewById<ViewGroup>(R.id.dialogView)

    val finalRadius = Math.max(parentView.width, parentView.height)

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      val revealAnimator = ViewAnimationUtils.createCircularReveal(
          parentView, cx, cy, finalRadius.toFloat(), 0f
      )
      revealAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
          super.onAnimationEnd(animation)
          dialog.dismiss()
          cView.visibility = View.INVISIBLE
          fabSave.backToPosition()
        }
      })
      revealAnimator.duration = 400
      revealAnimator.start()
    } else {
      dialog.dismiss()
    }
  }

  private fun revealDialog(cView: View) {
    Logger.d("Cx $cx Cy $cy")
    val parentView = cView.findViewById<ViewGroup>(R.id.dialogView)

    val finalRadius = Math.max(parentView.width, parentView.height)

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      val revealAnimator = ViewAnimationUtils.createCircularReveal(
          parentView, cx, cy, 0f, finalRadius.toFloat()
      )
      parentView.visibility = VISIBLE
      revealAnimator.duration = 400
      revealAnimator.start()
    }
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
