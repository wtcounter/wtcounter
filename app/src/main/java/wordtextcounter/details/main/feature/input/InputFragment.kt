package wordtextcounter.details.main.feature.input

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_input.*
import kotlinx.android.synthetic.main.report_folded.*
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.base.BaseViewModel
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import wordtextcounter.details.main.store.ReportDatabase
import wordtextcounter.details.main.util.EditReport
import wordtextcounter.details.main.util.NoEvent
import wordtextcounter.details.main.util.RxBus
import wordtextcounter.details.main.util.extensions.backToPosition
import wordtextcounter.details.main.util.extensions.hideKeyboard
import wordtextcounter.details.main.util.extensions.onClick
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

  var reportNameEditMode: String? = null

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

    fabSave onClick showDialog()

//    ivMoreStats.setOnClickListener {
//      val dialogFragment = ExtraStatsFragment()
//      dialogFragment.show(fragmentManager, ExtraStatsFragment::class.java.name)
//    }

    disposable.add(RxTextView
        .textChanges(etInput)
        .debounce(300, MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          viewModel.calculateInput(it.toString())
        })

    viewModel.viewState.subscribe {
      it?.let { it1 -> handleViewState(it1) }
    }
    viewModel.additionLiveData.subscribe {
      it?.let {
        if (it) {
          activity?.hideKeyboard()
          activity?.showSnackBar(getString(R.string.addition_success))
          clearCurrentInputState()
        }
      }
    }
    viewModel.updateLiveData.subscribe {
      it?.let {
        if (it) {
          activity?.hideKeyboard()
          activity?.showSnackBar(getString(R.string.update_success))
          clearCurrentInputState()
        }
      }
    }
  }

  private fun clearCurrentInputState() {
    etInput.text = null
    reportNameEditMode = null
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

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

      }

    })


    reportNameEditMode?.let {
      etName.setText(it)
      etName.post { etName.setSelection(it.length) }
    }

    btnSave.setOnClickListener {
      if (!etName.text.isEmpty()) {
        viewModel.onClickSaveCurrent(etName.text.toString())
        hideDialog(cView, dialog)
      }
    }

    dialog.setCanceledOnTouchOutside(false)
//    dialog.window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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

  override fun onStart() {
    super.onStart()
    disposable.add(RxBus.subscribe(EditReport::class.java, Consumer {
      RxBus.send(NoEvent)
      reportNameEditMode = it.report.name
      if (etInput.text.trim().isNotEmpty()) {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.edit_alert_title)
            .setMessage(R.string.edit_alert_desc)
            .setPositiveButton(R.string.yes,
                { dialog, _ ->
                  etInput.setText(it.report.dataText)
                  dialog.dismiss()
                })
            .setNegativeButton(R.string.no,
                { dialog, _ -> dialog.dismiss() })
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setOnCancelListener {
              viewModel.cancelEdit()
              reportNameEditMode = null
            }
            .create().show()
      } else {
        etInput.setText(it.report.dataText)
      }
    }))

  }

  private fun handleViewState(viewState: ViewState) {
    if (viewState.showError) {
      showError(viewState.errorMessage)
    }

    tvCharacters.text = viewState.report?.characters
    tvWords.text = viewState.report?.words
    tvSentences.text = viewState.report?.sentences
    tvParagraphs.text = viewState.report?.paragraphs
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
