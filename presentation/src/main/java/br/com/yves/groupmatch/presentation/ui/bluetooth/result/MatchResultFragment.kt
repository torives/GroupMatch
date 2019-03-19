package br.com.yves.groupmatch.presentation.ui.bluetooth.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel

class MatchResultFragment : Fragment() {

	private var matchResult: MatchResultViewModel? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		arguments?.let {
			matchResult = it.getSerializable(ARG_MATCH_RESULT) as? MatchResultViewModel
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_matchresult_list, container, false)

		// Set the adapter
		if (view is RecyclerView) {
			with(view) {
				layoutManager = LinearLayoutManager(context)
				matchResult?.let {
					adapter = MatchResultAdapter(it)
				}
			}
		}
		return view
	}

	companion object {
		const val ARG_MATCH_RESULT = "arg_match_result"
	}
}
