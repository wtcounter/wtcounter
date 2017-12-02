package wordtextcounter.details.main.feature.input

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import wordtextcounter.details.main.MainActivity.ToolbarTitle
import wordtextcounter.details.main.R
import wordtextcounter.details.main.feature.base.BaseFragment
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : BaseFragment() {

  lateinit var viewModel: InputViewModel

  @BindView(R.id.etInput) lateinit var etInput: TextInputEditText;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)

    viewModel = ViewModelProviders.of(this).get(InputViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater!!.inflate(R.layout.fragment_input, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    RxBus.instance.send(ToolbarTitle(R.string.title_input))

    viewModel.viewState.observe(this, Observer { Log.d("No of chars ", it?.noOfChars.toString()) })
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.confirm -> {
        viewModel.confirmClicked(etInput.text.toString())
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  override fun onDetach() {
    super.onDetach()
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_fragment_input, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }


  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputFragment.
     */
    // TODO: Rename and change types and number of parameters
    fun newInstance(): InputFragment {
      val fragment = InputFragment()
      return fragment
    }
  }
}// Required empty public constructor
