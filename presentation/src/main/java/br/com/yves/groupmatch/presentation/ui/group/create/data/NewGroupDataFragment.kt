package br.com.yves.groupmatch.presentation.ui.group.create.data


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.ui.group.details.GridAutofitLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group_details.*

class NewGroupDataFragment : Fragment() {
	private val args: NewGroupDataFragmentArgs by navArgs()
	private lateinit var adapter: GroupMemberAdapter2

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_group_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.new_group_details_toolbarSubtitle)

		adapter = GroupMemberAdapter2(args.viewModel.members, Glide.with(this))
		new_group_details_membersRecyclerView.layoutManager = GridAutofitLayoutManager(context!!, 250)
		new_group_details_membersRecyclerView.adapter = adapter

		new_group_details_checkButton.setOnClickListener {
			onCheckButtonClick()
		}
	}
	//endregion

	private fun onCheckButtonClick() {
		val name = new_group_details_nameEditText.text.toString()
	}
}
