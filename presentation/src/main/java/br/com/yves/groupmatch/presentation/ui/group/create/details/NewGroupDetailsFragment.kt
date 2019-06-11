package br.com.yves.groupmatch.presentation.ui.group.create.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.group.create.UserViewModel
import br.com.yves.groupmatch.presentation.ui.group.details.GridAutofitLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group_details.*

class NewGroupDetailsFragment : Fragment() {
	private lateinit var adapter: GroupMemberAdapter2

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_group_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.new_group_details_toolbarSubtitle)

		adapter = GroupMemberAdapter2(listOf(
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg"),
				UserViewModel("Fulaninho de Tal", "", false,"https://i.redd.it/4fz0ct0l7mo11.jpg")
		), Glide.with(this))

		new_group_details_membersRecyclerView.layoutManager = GridAutofitLayoutManager(context!!, 250)
		new_group_details_membersRecyclerView.adapter = adapter
	}
	//endregion
}
