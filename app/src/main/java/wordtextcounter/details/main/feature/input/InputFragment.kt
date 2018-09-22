package wordtextcounter.details.main.feature.input

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipData.Item
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.edit
import com.jakewharton.rxbinding2.widget.RxTextView
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_input.cl
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
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.extensions.backToPosition
import wordtextcounter.details.main.util.extensions.hideKeyboard
import wordtextcounter.details.main.util.extensions.onClick
import wordtextcounter.details.main.util.extensions.showKeyBoard
import wordtextcounter.details.main.util.extensions.showSnackBar
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.util.ChangeDraftState
import wordtextcounter.details.main.util.Constants.PREF_SAVED_TEXT
import wordtextcounter.details.main.util.EditDraft
import wordtextcounter.details.main.util.EditDraftHistory
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.NewText
import wordtextcounter.details.main.util.NoEvent
import wordtextcounter.details.main.util.RateUsHelper.showRateUsDialog
import wordtextcounter.details.main.util.RxBus
import wordtextcounter.details.main.util.extensions.getPreference
import wordtextcounter.details.main.util.extensions.runIfAdded
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {

  private lateinit var viewModel: InputViewModel

  private lateinit var viewModelFactory: InputViewModelFactory

  var cx: Int = -1
  var cy: Int = -1
  var isPaste = true

  var reportNameEditMode: String? = null
  private lateinit var clipData: ClipData

  // Get clip data from clipboard.

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val dm = DisplayMetrics()
    activity?.windowManager?.defaultDisplay?.getMetrics(dm)

    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    val statusBarHeight = resources.getDimensionPixelSize(resourceId)

    cx = dm.widthPixels / 2
    cy = dm.heightPixels / 2 - statusBarHeight

    viewModelFactory = InputViewModelFactory(
        ReportDatabase.getInstance(activity?.applicationContext!!).reportDao(),
        ReportDatabase.getInstance(activity?.applicationContext!!).draftDao()
    )
    viewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(InputViewModel::class.java)

    val clipboardService = context?.getSystemService(CLIPBOARD_SERVICE)
    val clipboardManager = clipboardService as ClipboardManager
    clipData = clipboardManager.primaryClip
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

    fabSave onClick showDialog()

    foldingCell.initialize(500, ContextCompat.getColor(context!!, R.color.folder_back_side), 0)

    ivExpand onClick toggleCell()

    disposable.add(RxTextView
        .textChanges(etInput)
        .debounce(300, MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          viewModel.calculateInput(it.toString())
          if (it.isEmpty()) {
            viewModel.onTextCleared()
          }
        })


    viewModel.viewState.subscribe {
      it?.let { it1 -> handleViewState(it1) }
    }
    viewModel.additionLiveData.subscribe {
      it?.let {
        if (it) {
          activity?.hideKeyboard(etInput)
          activity?.showSnackBar(getString(R.string.addition_success))
          clearCurrentInputState()
          this@InputFragment.activity?.let { it1 -> showRateUsDialog(it1) }
        }
      }
    }
    viewModel.updateLiveData.subscribe {
      it?.let {
        if (it) {
          activity?.hideKeyboard(etInput)
          activity?.showSnackBar(getString(R.string.update_success))
          clearCurrentInputState()
        }
      }
    }

    prefillSavedPreferenceText()

    if (isPaste) {
      activity?.hideKeyboard(etInput)
      paste()
      isPaste = false
    } else {
      cl.clearFocus()
      etInput.requestFocus()
      activity?.showKeyBoard()
    }
  }

  private fun prefillSavedPreferenceText() {
    val preferences = context?.getPreference()
    val prefilledText = preferences?.getString(PREF_SAVED_TEXT, "")
    if (!prefilledText.isNullOrEmpty()) {
      etInput.setText(prefilledText)
      clearSavedTextInPreference()
    }
  }

  private fun paste() {
    // Get item count.

    val itemCount: Int = clipData.itemCount
    if (itemCount > 0) {
      val item: Item = clipData.getItemAt(0)
      val copiedText: String = item.text.toString();
      // Show a snackbar to tell user text has been pasted.
      showSnackBar(copiedText = copiedText)
    } else {
      cl.clearFocus()
      etInput.requestFocus()
      activity?.showKeyBoard()
    }
  }

  private fun showSnackBar(copiedText: String) {
    val snackbar: Snackbar = Snackbar.make(etInput, copiedText, Snackbar.LENGTH_INDEFINITE)
    val layout = snackbar.view as Snackbar.SnackbarLayout

    layout.setOnClickListener {
      snackbar.dismiss()
      etInput.requestFocus()
      activity?.showKeyBoard()
    }

    snackbar.setAction("PASTE") {
      RxBus.send(NewText(copiedText))
    }

    snackbar.show()
  }

  private fun clearCurrentInputState() {
    etInput.text = null
    reportNameEditMode = null
  }

  private fun toggleCell(): () -> Unit = {
    foldingCell.toggle(false)
  }

  private fun showDialog(): () -> Unit = {
    logAnalytics(Click("fab_add_click"))
    val cView = LayoutInflater.from(activity)
        .inflate(R.layout.report_name_edit, null)

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(cView)

    dialog.setOnShowListener {
      revealDialog(cView)
    }

    dialog.setOnKeyListener { dialog1, keyCode, _ ->
      if (keyCode == KeyEvent.KEYCODE_BACK) {
        hideDialog(cView, dialog1)
        return@setOnKeyListener true
      }
      false
    }

    val etName = dialog.findViewById<AppCompatEditText>(R.id.etName)
    val btnSave = dialog.findViewById<Button>(R.id.btnSave)
    val ivCross = dialog.findViewById<ImageView>(R.id.ivCross)
    ivCross.setOnClickListener {
      logAnalytics(Click("note_add_dialog_close"))
      hideDialog(cView, dialog)
    }

    etName.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        if (s != null && !s.isEmpty()) {
          context?.let { btnSave.setTextColor(ContextCompat.getColor(it, R.color.secondaryColor)) }
          btnSave.isEnabled = true
        } else {
          context?.let {
            btnSave.setTextColor(ContextCompat.getColor(it, R.color.saveButtonDisabled))
          }
          btnSave.isEnabled = false
        }
      }

      override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
      ) {

      }

      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
      ) {

      }

    })


    reportNameEditMode?.let {
      etName.setText(it)
      etName.post { etName.setSelection(it.length) }
    }

    btnSave.setOnClickListener {
      if (!etName.text.isEmpty()) {
        logAnalytics(Click("note_add_dialog_save"))
        viewModel.onClickSaveCurrent(etName.text.toString())
        hideDialog(cView, dialog)
      }
    }

    dialog.setCanceledOnTouchOutside(false)
//    dialog.window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
  }

  fun saveDraft() {
    runIfAdded {
      val editable = etInput.text
      if (editable != null) {
        viewModel.addOrUpdateDraftIfTextChanged(editable.toString())
      }
    }
  }

  fun saveCurrentTextToPreference() {
    runIfAdded {
      val editable = etInput.text
      if (editable != null) {
        val preferences = context?.getPreference()
        preferences?.edit {
          putString(PREF_SAVED_TEXT, editable.toString())
        }
      }
    }
  }

  private fun clearSavedTextInPreference() {
    val preferences = context?.getPreference()
    preferences?.edit {
      putString(PREF_SAVED_TEXT, null)
    }
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
      fabSave.backToPosition()
    }
  }

  private fun revealDialog(cView: View) {
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

  private fun handleNewTextEvent(text: String, overrideListener: TextOverrideListener? = null) {
    if (etInput.text.trim().isNotEmpty()) {
      AlertDialog.Builder(context!!)
          .setTitle(R.string.edit_alert_title)
          .setMessage(R.string.edit_alert_desc)
          .setPositiveButton(R.string.yes
          ) { dialog, _ ->
            logAnalytics(Click("update_warning_dialog_yes"))
            etInput.setText(text)
            etInput.requestFocus()
            activity?.showKeyBoard()
            viewModel.onTextCleared()
            overrideListener?.onTextOverride()
            dialog.dismiss()
          }
          .setNegativeButton(R.string.no
          ) { dialog, _ ->
            logAnalytics(Click("update_warning_dialog_no"))
            dialog.dismiss()
          }
          .setIcon(R.drawable.ic_warning_black_24dp)
          .setOnCancelListener {
            logAnalytics(Click("update_warning_dialog_cancel"))
            viewModel.cancelEdit()
            reportNameEditMode = null
          }
          .create()
          .show()
    } else {
      etInput.setText(text)
      overrideListener?.onTextOverride()
    }
  }

  override fun onStart() {
    super.onStart()
    Logger.d("Start.")
    disposable.add(RxBus.subscribe(EditReport::class.java, Consumer {
      RxBus.send(NoEvent)
      reportNameEditMode = it.report.name
      it.report.dataText?.let {
        RxBus.send(NewText(it))
      }
    }))

    disposable.add(RxBus.subscribe(NewText::class.java, Consumer {
      RxBus.send(NoEvent)
      handleNewTextEvent(it.newText)
    }))

    disposable.add(RxBus.subscribe(EditDraft::class.java, Consumer {
      RxBus.send(NoEvent)
      handleNewTextEvent(it.text, object : TextOverrideListener {
        override fun onTextOverride() {
          RxBus.send(ChangeDraftState(it.text, it.id))
        }
      })
    }))

    disposable.add(RxBus.subscribe(EditDraftHistory::class.java, Consumer {
      RxBus.send(NoEvent)
      handleNewTextEvent(it.text, object : TextOverrideListener {
        override fun onTextOverride() {
          RxBus.send(ChangeDraftState(it.parentDraftText, it.id))
        }
      })
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

  private interface TextOverrideListener {
    fun onTextOverride()
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
