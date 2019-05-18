package br.com.yves.groupmatch.presentation.ui.groups.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.group.GroupMockRepository
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import br.com.yves.groupmatch.presentation.ui.account.UserViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_group_detail.*

class GroupDetailsFragment : Fragment(), GroupDetailView {

	private val args: GroupDetailsFragmentArgs by navArgs()
	private lateinit var groupMemberAdapter: GroupMemberAdapter
	private lateinit var controller: GroupDetailsController

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_group_detail, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupRecyclerView()
		controller = GroupDetailsController(
				args.groupDetails.id,
				this,
				GroupDetailsPresenterImpl(),
				GroupMockRepository()
		)

		runOnBackground {
			controller.onViewCreated()
		}
	}
	//endregion

	private fun setupRecyclerView() {
		groupMemberAdapter = GroupMemberAdapter(null, Glide.with(this))
		group_details_recyclerview.adapter = groupMemberAdapter
		group_details_recyclerview.layoutManager = LinearLayoutManager(context)
		group_details_recyclerview.setHasFixedSize(true)
		group_details_recyclerview.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)
	}

	//region GroupDetailView
	override fun displayMatchButton() = runOnUiThread {
		group_details_answerMatch.visibility = VISIBLE
	}

	override fun hideMatchButton() = runOnUiThread {
		group_details_answerMatch.visibility = GONE
	}

	override fun displayGroupMembers(members: List<UserViewModel>) {
		groupMemberAdapter.updateMembers(members)
	}
	//endregion

}