package br.com.yves.groupmatch.presentation.ui.bluetooth.result

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R

import br.com.yves.groupmatch.presentation.ui.bluetooth.result.dummy.DummyContent
import br.com.yves.groupmatch.presentation.ui.bluetooth.result.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [MatchResultFragment.OnListFragmentInteractionListener] interface.
 */
class MatchResultFragment : Fragment() {

	// TODO: Customize parameters
	private var columnCount = 1

	private var listener: OnListFragmentInteractionListener? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			columnCount = it.getInt(ARG_COLUMN_COUNT)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_matchresult_list, container, false)

		// Set the adapter
		if (view is RecyclerView) {
			with(view) {
				layoutManager = when {
					columnCount <= 1 -> LinearLayoutManager(context)
					else -> GridLayoutManager(context, columnCount)
				}
				adapter = MatchResultAdapter(DummyContent.ITEMS, listener)
			}
		}
		return view
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
//		if (context is OnListFragmentInteractionListener) {
//			listener = context
//		} else {
//			throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
//		}
	}

	override fun onDetach() {
		super.onDetach()
		listener = null
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 *
	 *
	 * See the Android Training lesson
	 * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
	 * for more information.
	 */
	interface OnListFragmentInteractionListener {
		// TODO: Update argument type and name
		fun onListFragmentInteraction(item: DummyItem?)
	}

	companion object {

		// TODO: Customize parameter argument names
		const val ARG_COLUMN_COUNT = "column-count"

		// TODO: Customize parameter initialization
		@JvmStatic
		fun newInstance(columnCount: Int) =
				MatchResultFragment().apply {
					arguments = Bundle().apply {
						putInt(ARG_COLUMN_COUNT, columnCount)
					}
				}
	}
}
