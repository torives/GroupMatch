package br.com.yves.groupmatch.presentation.ui.groups.create


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.data.user.FirestoreUserRepository
import br.com.yves.groupmatch.domain.user.User
import br.com.yves.groupmatch.domain.user.UserRepository
import br.com.yves.groupmatch.presentation.runOnBackground
import br.com.yves.groupmatch.presentation.runOnUiThread
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_new_group.*
import java.lang.ref.WeakReference

interface NewGroupView {
	fun displayUsers(users: List<UserViewModel>)
}

class NewGroupController(
		view: NewGroupView,
		private val repository: UserRepository
) {
	private val selectedUsers = mutableListOf<User>()
	private val users = mutableListOf<User>()
	private val viewRef = WeakReference(view)
	private val view: NewGroupView?
		get() = viewRef.get()

	fun onViewCreated() {}
	fun onUserSelected(user: UserViewModel) {}
}

class NewGroupFragment : Fragment(), NewGroupView, UserAdapter.Listener {

	private lateinit var adapter: UserAdapter
	private lateinit var controller: NewGroupController

	//region Lifecycle
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_new_group, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		controller = NewGroupController(this, FirestoreUserRepository())

		adapter = UserAdapter(Glide.with(this), listener = this)
		new_group_userRecyclerView.layoutManager = LinearLayoutManager(context)
		new_group_userRecyclerView.adapter = adapter
	}
	//endregion

	//region NewGroupView
	override fun displayUsers(users: List<UserViewModel>) = runOnUiThread {
		adapter.update(users)
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
