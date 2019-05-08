package br.com.yves.groupmatch.debug


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.yves.groupmatch.R

/**
 * A simple [Fragment] subclass.
 */
class GroupDetailFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_group_detail, container, false)
	}


}
