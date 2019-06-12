package br.com.yves.groupmatch.presentation.ui.group.create


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import br.com.yves.groupmatch.presentation.ui.group.create.data.NewGroupDetailsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group.*


class NewGroupFragment : Fragment(), NewGroupView, UserAdapter.Listener {

	private lateinit var adapter: UserAdapter
	private lateinit var controller: NewGroupController

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_group, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupToolbar()
		setupRecyclerView()
		setupListeners()

		//TODO: Create NewGroupInjection
		controller = NewGroupController(this, FirestoreUserRepository(), UserPresenterImpl())

		runOnBackground {
			controller.onViewCreated()
		}
	}

	private fun setupToolbar() {
		(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.new_group_toolbarSubtitle)
	}

	private fun setupListeners() {
		new_group_nextButton.setOnClickListener {
			runOnBackground {
				controller.onNextButtonClick()
			}
		}
	}

	private fun setupRecyclerView() {
		adapter = UserAdapter(Glide.with(this), listener = this)
		new_group_userRecyclerView.layoutManager = LinearLayoutManager(context)
		new_group_userRecyclerView.adapter = adapter
	}
	//endregion

	//region NewGroupView
	override fun displayUsers(users: List<UserViewModel>) = runOnUiThread {
		adapter.update(users)
	}

	override fun displayNextButton() = runOnUiThread {
		new_group_nextButton.show()
	}

	override fun hideNextButton() = runOnUiThread {
		new_group_nextButton.hide()
	}

	override fun navigateToNewGroupDetails(viewModel: NewGroupDetailsViewModel) = runOnUiThread {
		val action = NewGroupFragmentDirections.actionNewGroupFragmentToNewGroupDetails(viewModel)
		findNavController().navigate(action)
	}
	//endregion

	//region UserAdapter.Listener
	override fun onUserSelected(user: UserViewModel) {
		runOnBackground {
			controller.onUserSelected(user)
		}
	}
	//endregion
}
