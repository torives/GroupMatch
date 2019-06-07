package br.com.yves.groupmatch.presentation.ui.groups.create


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
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

		(activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.new_group_toolbarSubtitle)
		controller = NewGroupController(this, FirestoreUserRepository(), UserPresenterImpl())

		adapter = UserAdapter(Glide.with(this), listener = this)
		new_group_userRecyclerView.layoutManager = LinearLayoutManager(context)
		new_group_userRecyclerView.adapter = adapter

		new_group_nextButton.setOnClickListener {
			runOnBackground {
				controller.onNextButtonClick()
			}
		}

		runOnBackground {
			controller.onViewCreated()
		}
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
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
