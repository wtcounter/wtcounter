package wordtextcounter.details.main.feature.notes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wordtextcounter.details.main.R
import wordtextcounter.details.main.R.string
import wordtextcounter.details.main.feature.main.MainActivity.ToolbarTitle
import wordtextcounter.details.main.util.RxBus

/**
 * A simple [Fragment] subclass.
 * Use the [NotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesFragment : Fragment() {

  lateinit var viewModel: NotesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    RxBus.instance.send(ToolbarTitle(string.title_notes))

    viewModel.getAllSavedNotes()
  }

  companion object {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotesFragment.
     */
    fun newInstance(): NotesFragment {
      val fragment = NotesFragment()
      val args = Bundle()
      fragment.arguments = args
      return fragment
    }
  }
}// Required empty public constructor
