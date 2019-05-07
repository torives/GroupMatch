package br.com.yves.groupmatch.presentation.ui.groups


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.presentation.injection.GroupInjection
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupFragment : Fragment(), GroupsView {

	private val controller = GroupInjection().make(this)
	private lateinit var groupAdapter: GroupAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_groups, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupRecyclerView()

		runOnBackground {
			controller.onViewCreated()
		}
	}

	private fun setupRecyclerView() {
		groupAdapter = GroupAdapter(null, Glide.with(this))
		recyclerview_groups_list.adapter = groupAdapter
		recyclerview_groups_list.layoutManager = LinearLayoutManager(context)
		recyclerview_groups_list.setHasFixedSize(true)
		recyclerview_groups_list.addItemDecoration(
				DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
		)
	}

	//region GroupsView
	override fun displayGroups(groups: List<GroupViewModel>) = runOnUiThread {
		groupAdapter.updateGroups(groups)
	}

	override fun displayLoggedOutLayout() = runOnUiThread {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion
}
