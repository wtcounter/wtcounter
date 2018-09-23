package wordtextcounter.details.main.feature.input

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_input.*
import kotlinx.android.synthetic.main.report_folded.*
import kotlinx.android.synthetic.main.report_summary.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.analytics.AnalyticsLogger.AnalyticsEvents.Click
import wordtextcounter.details.main.analytics.AnalyticsLogger.logAnalytics
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.*
import wordtextcounter.details.main.util.RateUsHelper.showRateUsDialog
import wordtextcounter.details.main.util.extensions.hideKeyboard
import wordtextcounter.details.main.util.extensions.showKeyBoard
import wordtextcounter.details.main.util.extensions.showSnackBar
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
  var reportIdEditMode: Int? = null
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

  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    ibAdd.setOnClickListener { showDialog() }

    ibExtraStats.setOnClickListener {
      viewModel.viewState.value.report?.dataText?.let { text ->
        RxBus.send(ExtraStatText(text))
        val dialogFragment = ExtraStatsFragment.newInstance()
        dialogFragment.show(fragmentManager, ExtraStatsFragment::class.java.name)
      }
    }

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

  private fun paste() {
    // Get item count.

    val itemCount: Int = clipData.itemCount
    if (itemCount > 0) {
      val item: Item = clipData.getItemAt(0)
      val copiedText: String = item.text.toString()
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
      etInput.setText(copiedText)
      cl.clearFocus()
      etInput.requestFocus()
      activity?.showKeyBoard()
    }

    snackbar.show()
  }

  private fun showButtons() {
    if (llButtons.visibility == GONE) {
      llButtons.visibility = VISIBLE
    }
  }

  private fun hideButtons() {
    if (llButtons.visibility == VISIBLE) {
      llButtons.visibility = GONE
    }
  }

  private fun clearCurrentInputState() {
    etInput.text = null
    cancelEditMode()
  }

  private fun cancelEditMode() {
    reportNameEditMode = null
    reportIdEditMode = null
    viewModel.cancelEdit()
  }

  private fun showDialog() {
    val cView = LayoutInflater.from(activity).inflate(R.layout.report_name_edit, null)
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
    if (!isAdded) {
      //if fragment is destroyed, then return silently
      return
    }

    val editable = etInput.text
    if (editable != null) {
      viewModel.addOrUpdateDraftIfTextChanged(editable.toString())
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
        }
      })
      revealAnimator.duration = 300
      revealAnimator.start()
    } else {
      dialog.dismiss()
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
      revealAnimator.duration = 300
      revealAnimator.start()
    }
  }

  private fun handleBusEditEvent(text: String, overrideListener: TextOverrideListener? = null) {
    if (etInput.text.trim().isNotEmpty()) {
      AlertDialog.Builder(context!!)
          .setTitle(R.string.edit_alert_title)
          .setMessage(R.string.edit_alert_desc)
          .setPositiveButton(R.string.yes
          ) { dialog, _ ->
            logAnalytics(Click("update_warning_dialog_yes"))
            etInput.setText(text)
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
            cancelEditMode()
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
    disposable.add(RxBus.subscribe(EditReport::class.java, Consumer {
      RxBus.send(NoEvent)
      reportNameEditMode = it.report.name
      reportIdEditMode = it.report.id
      it.report.dataText?.let { it1 -> handleBusEditEvent(it1) }
    }))

    disposable.add(RxBus.subscribe(EditDraft::class.java, Consumer {
      RxBus.send(NoEvent)
      handleBusEditEvent(it.text, object : TextOverrideListener {
        override fun onTextOverride() {
          RxBus.send(ChangeDraftState(it.text, it.id))
        }
      })
    }))

    disposable.add(RxBus.subscribe(EditDraftHistory::class.java, Consumer {
      RxBus.send(NoEvent)
      handleBusEditEvent(it.text, object : TextOverrideListener {
        override fun onTextOverride() {
          RxBus.send(ChangeDraftState(it.parentDraftText, it.id))
        }
      })
    }))

    disposable.add(RxBus.subscribe(ShareText::class.java, Consumer {
      RxBus.send(NoEvent)
      if (it?.shareText != null) etInput.setText(it.shareText)
    }))

    disposable.add(RxBus.subscribe(DeleteReport::class.java, Consumer {
      if (it.report.id == reportIdEditMode) {
        cancelEditMode()
      }
    }))
  }

  private fun handleViewState(viewState: ViewState) {
    if (viewState.showError) {
      showError(viewState.errorMessage)
    }

    if (viewState.showAddExpand) {
      showButtons()
      tvCharacters.text = viewState.report?.characters
      tvWords.text = viewState.report?.words
      tvSentences.text = viewState.report?.sentences
      tvParagraphs.text = viewState.report?.paragraphs
    } else {
      hideButtons()
      tvCharacters.text = "-"
      tvWords.text = "-"
      tvSentences.text = "-"
      tvParagraphs.text = "-"
    }
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
