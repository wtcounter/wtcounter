package wordtextcounter.details.main.feature.input

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_input.etInput
import kotlinx.android.synthetic.main.fragment_input.toolbar
import kotlinx.android.synthetic.main.report_summary.ibSave
import kotlinx.android.synthetic.main.report_summary.tvCharacters
import kotlinx.android.synthetic.main.report_summary.tvSentences
import kotlinx.android.synthetic.main.report_summary.tvWords
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.feature.input.InputViewModel.ViewState
import java.util.concurrent.TimeUnit.MILLISECONDS
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import kotlinx.android.synthetic.main.report_name_edit.*
import wordtextcounter.details.main.store.ReportDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {
  
  lateinit var viewModel: InputViewModel
  
  private val TEXT = "TEXT"
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this,
        InputViewModelFactory(ReportDatabase.getInstance(activity?.applicationContext!!)?.reportDao()!!)).get(InputViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_input, container, false)
  }
  
  @SuppressLint("ClickableViewAccessibility", "RxSubscribeOnError", "RxDefaultScheduler", "InflateParams")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    
    ibSave.setOnClickListener {
      MaterialStyledDialog.Builder(activity)
          .setTitle("") // This is intentional. Not providing this results into weird UI.
          .setDescription((activity as AppCompatActivity).getString(R.string.save_dialog_desc))
          .withDarkerOverlay(true)
          .setPositiveText((activity as AppCompatActivity).getString(R.string.bookmark))
          .setCustomView(LayoutInflater.from(activity).inflate(R.layout.report_name_edit, null), 20, 20, 20 ,20)
          .onPositive { _, _ ->
            viewModel.onClickSaveCurrent(rName.text.toString())
          }
          .setIcon(R.drawable.note_add)
          .show()
    }
    
    etInput.setText(savedInstanceState?.getString(TEXT))
    
    disposable.add(RxTextView
        .textChanges(etInput)
        .debounce(400, MILLISECONDS) // default Scheduler is Computation
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
    
    tvCharacters.text = viewState.noOfCharacters
    tvWords.text = viewState.noOfWords
    tvSentences.text = viewState.noOfSentences
    
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
