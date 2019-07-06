package br.com.yves.groupmatch.presentation.ui.groups.create


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.auth.GroupMatchAuth
import br.com.yves.groupmatch.data.group.FirestoreGroupRepository
import br.com.yves.groupmatch.data.match.FirestoreMatchRepository
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
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
		val userRepository = FirestoreUserRepository()
//		controller = NewGroupController(
//				this,
//				userRepository,
//				FirestoreGroupRepository(userRepository, FirestoreMatchRepository()),
//				GroupMatchAuth.instance,
//				UserPresenterImpl()
//		)

		runOnBackground {
			controller.onViewCreated()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		(activity as AppCompatActivity).supportActionBar?.subtitle = ""
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

	override fun navigateToNewGroupDetails() = runOnUiThread {

		val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
			leftMargin = 64
			rightMargin = 64
		}
		val editText = EditText(context!!).apply {
			setSingleLine()
			layoutParams = params
		}
		val container = FrameLayout(context!!)
		container.addView(editText)

		val alert = AlertDialog.Builder(context!!)
				.setTitle("Escolha um nome para o novo grupo:")
				.setView(container)
				.setPositiveButton("Criar") { dialog, _ ->
					val groupName = editText.text.toString()

					runOnBackground {
						controller.onCreateGroupAttempt(groupName)
					}
				}
				.setNegativeButton("Cancelar") { _, _ ->
					Log.d("", "")
				}
				.create()

		alert.show()
//		val action = NewGroupFragmentDirections.actionNewGroupFragmentToNewGroupDetails(viewModel)
//		findNavController().navigate(action)
	}

	override fun navigateToGroups() {
		findNavController().popBackStack()
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
