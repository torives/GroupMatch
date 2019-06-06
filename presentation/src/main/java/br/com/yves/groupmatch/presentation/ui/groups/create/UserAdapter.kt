package br.com.yves.groupmatch.presentation.ui.groups.create

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class UserAdapter(
		private var items: List<UserViewModel> = listOf(),
		private val listener: Listener
) : RecyclerView.Adapter<UserAdapter.UserListViewHolder>() {

	private var selectedUsers = mutableListOf<UserListItem.User>()
	private var users = mutableListOf<UserListItem.User>()

	//region RecyclerView.Adapter
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getItemViewType(position: Int): Int {
		return if(position == 0) {
			if(selectedUsers.isEmpty()) {

			}
			UserListItem.Header.type
		} else {
			return 0
		}
	}

	override fun getItemCount(): Int {
		return if(selectedUsers.isEmpty()) {
			users.size + 1
		} else {
			selectedUsers.size + users.size + 2
		}
	}

	override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	//endregion

	interface Listener {
		fun onItemSelected(item: UserListItem)
	}

	abstract inner class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		abstract fun bind(item: UserListItem)
	}
}


sealed class UserListItem(val type: Int) {
	data class Header(val title: String) : UserListItem(type) {
		companion object {
			const val type = 1
		}
	}

	data class User(
			val viewModel: UserViewModel,
			var isSelected: Boolean
	) : UserListItem(type) {
		companion object {
			const val type = 2
		}
	}
}

class UserViewModel(
		val name: String,
		val email: String,
		var isSelected: Boolean,
		val profileImageURL: String? = null
)