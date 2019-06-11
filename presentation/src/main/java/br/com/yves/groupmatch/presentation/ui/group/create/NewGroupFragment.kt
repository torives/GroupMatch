package br.com.yves.groupmatch.presentation.ui.group.create


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import br.com.yves.groupmatch.presentation.ui.group.create.data.NewGroupDetailsViewModel


class NewGroupFragment : Fragment(), NewGroupView, UserAdapter.Listener {

	private lateinit var adapter: UserAdapter
	private lateinit var controller: NewGroupController
	private lateinit var newGroupDataViewModel: NewGroupDataViewModel

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_group, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupToolbar()
		setupViewModel()
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

	private fun setupViewModel() {
		newGroupDataViewModel = ViewModelProviders.of(this).get(NewGroupDataViewModel::class.java)
		newGroupDataViewModel.groupName.observe(this, Observer<String> {

		})
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

		ViewModelProviders.of(this).get()

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

class NewGroupDataViewModel: ViewModel() {
	var groupName = MutableLiveData<String>()
	var groupImageURL = MutableLiveData<String>()

	fun setGroupData(name: String, imageURL: String?) {
		groupName.value = name
		groupImageURL.value = imageURL
	}
}