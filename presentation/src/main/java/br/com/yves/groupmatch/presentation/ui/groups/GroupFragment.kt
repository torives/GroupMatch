package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.injection.GroupInjection
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import br.com.yves.groupmatch.presentation.ui.bluetooth.result.MatchResultFragment
import br.com.yves.groupmatch.presentation.ui.bluetooth.server.MatchResultViewModel
import br.com.yves.groupmatch.presentation.ui.groups.details.GroupDetailsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_groups.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class GroupFragment : Fragment(),
		GroupView,
		GroupAdapter.SelectionListener {

	private val controller = GroupInjection().make(this)
	private lateinit var groupAdapter: GroupAdapter

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		setHasOptionsMenu(true)
		return inflater.inflate(R.layout.fragment_groups, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupRecyclerView()

		runOnBackground {
			controller.onViewCreated()
		}
	}

	override fun onResume() {
		super.onResume()
		runOnBackground {
			controller.onViewResumed()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.group_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId) {
			R.id.group_menu_newGroup_action -> runOnBackground { controller.onGroupCreationAttempt() }
		}
		return super.onOptionsItemSelected(item)
	}
	//endregion

	private fun setupRecyclerView() {
		groupAdapter = GroupAdapter(null, Glide.with(this), this)
		recyclerview_groups_list.adapter = groupAdapter
		recyclerview_groups_list.layoutManager = LinearLayoutManager(context)
		recyclerview_groups_list.setHasFixedSize(true)
		recyclerview_groups_list.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)
	}

	//region GroupView
	override fun displayGroups(groups: List<GroupViewModel>) = runOnUiThread {
		fragment_group_loggedOutLayout.visibility = GONE
		recyclerview_groups_list.visibility = VISIBLE

		groupAdapter.updateGroups(groups)
	}

	override fun displayLoggedOutLayout() = runOnUiThread {
		fragment_group_loggedOutLayout.visibility = VISIBLE
		recyclerview_groups_list.visibility = GONE
	}

	override fun displayDialog(title: String, message: String, onPositiveResponse: () -> Unit, onNegativeResponse: () -> Unit) {
		activity?.runOnUiThread {
			alert(message, title) {
				yesButton { runOnBackground { onPositiveResponse.invoke() } }
				noButton { runOnBackground { onNegativeResponse.invoke() } }
			}.apply {
				isCancelable = false
				show()
			}
		}
	}

	override fun navigateToGroupDetails(details: GroupDetailsViewModel) = runOnUiThread {
		val action = GroupFragmentDirections.actionGroupsFragmentToGroupDetailFragment(details)
		findNavController().navigate(action)
	}

	override fun navigateToNewGroup() = runOnUiThread {
		findNavController().navigate(R.id.newGroupFragment)
	}

	override fun navigateToMatchResult(result: MatchResultViewModel) = runOnUiThread {
		val args = Bundle().apply {
			putSerializable(MatchResultFragment.ARG_MATCH_RESULT, result)
		}
		findNavController().navigate(R.id.matchResultFragment, args)
	}
	//endregion

	//region GroupAdapter.SelectionListener
	override fun onGroupSelected(group: GroupViewModel) = runOnBackground {
		controller.onGroupSelected(group.id)
	}
	//endregion
}
